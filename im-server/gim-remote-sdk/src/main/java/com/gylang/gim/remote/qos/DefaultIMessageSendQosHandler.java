package com.gylang.gim.remote.qos;

import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.sys.AckMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.remote.SocketHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 处理消息接收，回复客户端ack，保证服务能接收到消息ack
 *
 * @author gylang
 * data 2021/3/3
 */
@Slf4j
public class DefaultIMessageSendQosHandler implements IMessageSenderQosHandler {

    private final ConcurrentMap<String, Long> messageTimeStamp = new ConcurrentHashMap<>();
    private ConcurrentSkipListMap<String, MessageWrap> sentMessages = new ConcurrentSkipListMap<>();


    /**
     * 定时任务扫码间隔
     */
    private int checkInterval = 5 * 1000;
    /**
     * 扫码最低时间间隔
     */
    private int messagesValidTime = 2 * 1000;
    /**
     * 定时扫码器
     */
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.AbortPolicy());


    private boolean executing = false;

    private int reSendNum = 3;


    @Override
    public void handle(MessageWrap message) {

        // 1. 非qos
        String msgId = message.getClientMsgId();

        // 2. ack = 1
        if (QosConstant.SEND_ACK1 == message.getAck()) {

            MessageWrap messageWrap = sentMessages.get(msgId);
            if (null != messageWrap) {
                // 存在重发记录 收到服务端ack1, 可以删除重发记录, qos2需要响应服务端
                sentMessages.remove(msgId);
                Long remove = messageTimeStamp.remove(msgId);
                if (log.isDebugEnabled()) {
                    log.debug("[qos1 - sender] : 接收到服务端ack1, 删除重发记录 : {}", null == remove ? "已经删除,这是重发" : "立即删除");
                }
            }
            // qos2 需要响应客户点 响应ack2 让客户端删除重发ack1列表
            if (null != messageWrap && QosConstant.ACCURACY_ONE_ARRIVE == message.getQos()) {
                AckMessage ackMessage = new AckMessage(SystemChatCmd.QOS_CLIENT_SEND_ACK, messageWrap);
                ackMessage.setAck(QosConstant.SEND_ACK2);
                SocketHolder.getInstance().writeAndFlush(ackMessage);
                if (log.isDebugEnabled()) {
                    log.debug("[qos2 - sender] : 接收到服务端ack1, 响应服务端ack2");
                }
            }
        }


    }

    @Override
    public boolean hasReceived(String msgId) {
        return messageTimeStamp.containsKey(msgId);
    }

    @Override
    public void addReceived(MessageWrap messageWrap) {
        if (!hasReceived(messageWrap.getClientMsgId())) {
            messageTimeStamp.put(messageWrap.getClientMsgId(), System.currentTimeMillis() + messagesValidTime);
            sentMessages.put(messageWrap.getClientMsgId(), messageWrap);
        }
    }

    @Override
    public void startup() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
                    if (!executing) {
                        executing = true;
                        scanReceive();
                        executing = false;
                    }
                },
                checkInterval,
                checkInterval,
                TimeUnit.MILLISECONDS);


    }

    @Override
    public void stop() {
        if (null != scheduledExecutorService) {
            scheduledExecutorService.shutdown();
        }
    }


    private void scanReceive() {

        /**
         * 清除qos队列
         *
         * @param receiveMessages   接收到的消息
         * @param checkInterval     检查间隔
         * @param messagesValidTime 消息过期时间
         */
        if (log.isDebugEnabled()) {

            log.debug("【QoS发送方】START 暂存处理线程正在运行中，当前长度" + messageTimeStamp.size() + ".");
        }

        //** 遍历清除
        long now = System.currentTimeMillis();
        Set<Map.Entry<String, MessageWrap>> entries = sentMessages.entrySet();
        Iterator<Map.Entry<String, MessageWrap>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MessageWrap> entry = iterator.next();
            String key = entry.getKey();
            MessageWrap msg = entry.getValue();
            // 删除接收消息表
            if (msg.getRetryNum() >= reSendNum) {
                if (log.isDebugEnabled()) {
                    log.debug("【QoS发送方】消息:msgId" + msg.getClientMsgId() + "的包已生存" + reSendNum
                            + "(最大允许" + reSendNum + "次数), 马上将删除之");
                    iterator.remove();
                    // 重发次数超出限制 响应失败
                    MessageWrap messageWrap = msg.copyBasic();
                    messageWrap.setClientMsgId(msg.getClientMsgId());
                    messageWrap.setMsg(msg.getMsgId());
                    messageWrap.setType(ChatTypeEnum.SYSTEM_MESSAGE.getType());
                    messageWrap.setCmd(SystemChatCmd.ERROR_MSG);
                    messageWrap.setCode(BaseResultCode.WEBSOCKET_CONNECTION_ERROR.getCode());
                    messageWrap.setMsg("消息发送失败");
                    SocketHolder.getInstance().sendBroadcast(messageWrap);

                }

            } else {
                //消息重发
                long timestamp = messageTimeStamp.get(key);
                MessageWrap messageWrap = sentMessages.get(key);
                if (now < timestamp) {
                    continue;
                }
                SocketHolder.getInstance().writeAndFlush(messageWrap);
                msg.setRetryNum(msg.getRetryNum() + 1);


            }
        }

        if (log.isDebugEnabled()) {

            log.debug("【QoS发送方】END 暂存处理线程正在运行中，当前长度" + messageTimeStamp.size() + ".");
        }
    }


}
