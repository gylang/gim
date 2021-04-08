package com.gylang.netty.sdk.handler.qos;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.model.IMSession;
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
public class DefaultIMessageReceiveQosHandler implements IMessageReceiveQosHandler {


    private final ConcurrentMap<String, Long> receiveMessages = new ConcurrentHashMap<>();
    /** 定时任务扫码间隔 */
    private int checkInterval = 10 * 1000;
    /** 消息超时时间 */
    private int messagesValidTime = 10 * 10 * 1000;
    /** 定时扫码器 */
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, ThreadFactoryBuilder.create().setNamePrefix("receive-qos-scanner").build());


    private boolean executing = false;


    @Override
    public boolean handle(MessageWrap message, IMSession target) {

        // qos2 接受方处理 响应ack1
        boolean add = addReceived(getKey(target.getAccount(), message.getClientMsgId()));
        MessageWrap messageWrap = message.copyBasic();
        messageWrap.setClientMsgId(message.getMsgId());
        messageWrap.setMsgId(message.getMsgId());
        messageWrap.setCmd(SystemChatCmd.QOS_RECEIVE_ACK);
        target.getSession().writeAndFlush(messageWrap);
        if (log.isDebugEnabled()) {
            log.debug("[qos2 - receiver] : 接收到服务端消息 , 响应服务端ack1");
        }
        return add;
    }

    @Override
    public boolean hasReceived(String senderId, String msgId) {
        return receiveMessages.containsKey(getKey(senderId, msgId));
    }

    @Override
    public void remove(String senderId, String msgId) {
        receiveMessages.remove(getKey(senderId, msgId));
    }

    @Override
    public boolean addReceived(String key) {
        // 不考虑并发 客户端去重
        if (receiveMessages.containsKey(key)) {
            return false;
        }
        receiveMessages.put(key, System.currentTimeMillis() + messagesValidTime);
        return true;
    }

    @Override
    public void startup() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
                    executing = true;
                    scanReceive();
                    executing = false;
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


    public void scanReceive() {

        if (log.isDebugEnabled()) {

            log.debug("【QoS接收方】START 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
        }

        //** 遍历清除
        for (Map.Entry<String, Long> entry : receiveMessages.entrySet()) {
            String key = entry.getKey();
            long value = entry.getValue();
            // 删除接收消息表
            long delta = System.currentTimeMillis() - value;
            if (delta >= messagesValidTime) {
                if (log.isDebugEnabled()) {
                    log.debug("【QoS接收方】指纹为" + key + "的包已生存" + delta
                            + "ms(最大允许" + messagesValidTime + "ms), 马上将删除之");
                }
                receiveMessages.remove(key);
            }
        }

        if (log.isDebugEnabled()) {

            log.debug("【QoS接收方】END 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
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

    }

    public String getKey(String senderId, String msgId) {

        return senderId + ":" + msgId;
    }
}
