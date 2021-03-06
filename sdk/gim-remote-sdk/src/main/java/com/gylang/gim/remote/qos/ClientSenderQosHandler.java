package com.gylang.gim.remote.qos;

import com.gylang.gim.api.domain.common.MessageWrap;

/**
 * 处理发出消息是否已经安全送达，实现消息的重发
 * 当消息间隔内未收到ack，消息重复
 *
 * @author gylang
 * data 2021/2/11
 */
public interface ClientSenderQosHandler {

    String CHECK_INTER_VAL_KEY = "IMessageSenderQosHandler.checkInterval";

    String MESSAGES_VALID_TIME_KEY = "IMessageSenderQosHandler.messagesValidTime";

    String SCAN_RECEIVE_KEY = "IMessageSenderQosHandler.scanReceive";

    String SCHEDULED_EXECUTOR_SERVICE = "IMessageSenderQosHandler.scheduledExecutorService";

    /**
     * 处处理消息
     *
     * @param messageWrap 消息包装类
     */
    void handle(MessageWrap messageWrap);

    /**
     * 是否已经接收到当前消息
     * @param msgId 消息序列号
     * @return 是否接收
     */
    boolean hasReceived(String msgId);

    /**
     * 添加已接收队列
     *
     * @param messageWrap 消息
     */
    void addReceived(MessageWrap messageWrap);

    /**
     * 启动扫码器
     */
    void startup();

    /**
     * 关闭扫码器
     */
    void stop();
}
