package com.gylang.gim.remote.qos;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.sys.AckMessage;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.remote.SocketHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 处理消息接收，回复客户端ack，保证服务能接收到消息ack
 *
 * @author gylang
 * data 2021/3/3
 */
@Slf4j
public class DefaultIClientReceiveQos2Handler implements ClientReceiveQos2Handler {


    private final ConcurrentMap<String, MessageWrap> receiveMessages = new ConcurrentHashMap<>();

    /** 定时任务扫码间隔 */
    private int checkInterval = 10 * 1000;
    /** 消息超时时间 */
    private int messagesValidTime = 10 * 10 * 1000;
    /** 定时扫码器 */
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, ThreadFactoryBuilder.create().setNamePrefix("receive-qos-scanner").build());

    private int reSendNum = 3;



    @Override
    public boolean handle(MessageWrap message) {

        // qos2 接受方处理 响应ack1
        boolean add = addReceived(message.getMsgId(), message);
        MessageWrap messageWrap = new AckMessage(message);
        messageWrap.setAck(QosConstant.RECEIVE_ACK1);
        messageWrap.setType(ChatTypeEnum.QOS_SERVER_SEND_ACK);
        SocketHolder.getInstance().writeAndFlush(messageWrap);
        if (log.isDebugEnabled()) {
            log.debug("[qos2 - receiver] : 接收到服务端消息 , 响应服务端ack1");
        }
        return add;
    }

    @Override
    public boolean hasReceived(String msgId) {
        return receiveMessages.containsKey(msgId);
    }

    @Override
    public void remove(String msgId) {
        receiveMessages.remove(msgId);
    }

    @Override
    public boolean addReceived(String key, MessageWrap message) {
        if (receiveMessages.containsKey(key)) {
            return false;
        }
        receiveMessages.put(key, message);
        return true;
    }

    @Override
    public void startup() {
        scheduledExecutorService.scheduleAtFixedRate(this::scanReceive,
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


    public void scanReceive() {

        if (log.isDebugEnabled()) {

            log.debug("【QoS接收方】START 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
        }

        //** 遍历清除


        for (Map.Entry<String, MessageWrap> entry : receiveMessages.entrySet()) {
            String key = entry.getKey();
            MessageWrap value = entry.getValue();

            // 删除接收消息表
            int retryNum = value.getRetryNum();
            value.setRetryNum(retryNum + 1);
            if (retryNum >= reSendNum) {
                if (log.isDebugEnabled()) {
                    log.debug("【QoS接收方】指纹为" + key + "的包已生存" + reSendNum
                            + "次(最大允许" + reSendNum + "次), 马上将删除之");
                }
                receiveMessages.remove(key);
            } else {
                SocketHolder.getInstance().writeAndFlush(value);
                if (log.isDebugEnabled()) {
                    log.debug("[qos2 - receiver] : [重发ack1] , 响应服务端ack0, msgId = {}", key);
                }
            }
        }

        if (log.isDebugEnabled()) {

            log.debug("【QoS接收方】END 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
        }
    }


}
