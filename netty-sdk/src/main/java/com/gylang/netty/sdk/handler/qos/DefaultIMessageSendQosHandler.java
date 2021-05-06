package com.gylang.netty.sdk.handler.qos;

import cn.hutool.core.util.StrUtil;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.sys.AckMessage;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
import io.netty.channel.Channel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 处理消息接收，回复客户端ack，保证服务能接收到消息ack
 *
 * @author gylang
 * data 2021/3/3
 */
@Slf4j
@Setter
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


    private EventProvider eventProvider;

    @Override
    public void handle(MessageWrap message, IMSession target) {

        // 1. 非qos
        String msgId = message.getMsgId();
        if (StrUtil.isEmpty(msgId)) {
            log.error("[主发消息msgId为空] : msg = {}", message);
        }
        // 2. ack = 1
        if (QosConstant.SEND_ACK1 == message.getAck()) {

            MessageWrap messageWrap = sentMessages.get(msgId);
            if (null != messageWrap && null != messageWrap.getReceive()
                    && messageWrap.getReceive().equals(target.getAccount())) {
                // 存在重发记录 收到ack0客户端收到, 客户端ack1可以删除重发记录, 并响应ack2
                sentMessages.remove(msgId);
                Long remove = messageTimeStamp.remove(msgId);
                if (log.isDebugEnabled()) {
                    log.debug("[qos1 - sender] : 接收到客户端ack1, 删除重发记录 : {}", null == remove ? "已经删除,这是重发" : "立即删除");
                }
            }
            // qos2 需要响应客户点 响应ack2 让客户端删除重发ack1列表
            if (null != messageWrap && QosConstant.ACCURACY_ONE_ARRIVE == messageWrap.getQos()) {
                AckMessage ackMessage = new AckMessage(SystemChatCmd.QOS_SERVER_SEND_ACK, messageWrap);
                ackMessage.setAck(QosConstant.SEND_ACK2);
                target.getSession().writeAndFlush(ackMessage);
                if (log.isDebugEnabled()) {
                    log.debug("[qos2 - sender] : 接收到客户端ack1, 响应客户端ack2");
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
        if (!hasReceived(messageWrap.getMsgId())) {
            messageTimeStamp.put(messageWrap.getMsgId(), System.currentTimeMillis() + messagesValidTime);
            sentMessages.put(messageWrap.getMsgId(), messageWrap);
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
        Iterator<Map.Entry<String, MessageWrap>> iterator = sentMessages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MessageWrap> entry = iterator.next();
            String key = entry.getKey();
            MessageWrap msg = entry.getValue();
            // 删除接收消息表
            if (msg.getRetryNum() >= reSendNum) {
                if (log.isDebugEnabled()) {
                    log.debug("【QoS发送方】消息:msgId" + msg.getMsgId() + "的包已生存" + reSendNum
                            + "(最大允许" + reSendNum + "次数), 马上将删除之");
                    iterator.remove();

                }

            } else {
                //消息重发
                long timestamp = messageTimeStamp.get(key);
                if (now < timestamp) {
                    continue;
                }
                Channel session = LocalSessionHolderUtil.getSession(msg.getReceive());
                if (null != session) {
                    session.writeAndFlush(msg);
                    msg.setRetryNum(msg.getRetryNum() + 1);
                } else {
                    // 用户离线
                    if (msg.isOfflineMsgEvent()) {
                        eventProvider.sendEvent(EventTypeConst.PERSISTENCE_MSG_EVENT, msg);
                        iterator.remove();
                    }
                }
            }

            if (log.isDebugEnabled()) {

                log.debug("【QoS发送方】END 暂存处理线程正在运行中，当前长度" + messageTimeStamp.size() + ".");
            }
        }
    }


    @Override
    public void init(NettyConfiguration configuration) {

        Integer customCheckInterval = configuration.getProperties(CHECK_INTER_VAL_KEY);
        this.checkInterval = (null == customCheckInterval || customCheckInterval <= 0)
                ? this.checkInterval : customCheckInterval;


        Integer customMessagesValidTime = configuration.getProperties(MESSAGES_VALID_TIME_KEY);
        this.messagesValidTime = (null == customMessagesValidTime || customMessagesValidTime <= 0)
                ? this.messagesValidTime : customMessagesValidTime;
        ScheduledExecutorService customScheduledExecutorService = configuration.getProperties(SCHEDULED_EXECUTOR_SERVICE);
        this.scheduledExecutorService = null == customScheduledExecutorService ? this.scheduledExecutorService : customScheduledExecutorService;
        this.eventProvider = configuration.getEventProvider();
    }
}
