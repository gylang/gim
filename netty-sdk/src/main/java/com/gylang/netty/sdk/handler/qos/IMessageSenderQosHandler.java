package com.gylang.netty.sdk.handler.qos;

import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;

/**
 * 处理发出消息是否已经安全送达，实现消息的重发
 * 当消息间隔内未收到ack，消息重复
 *
 * @author gylang
 * data 2021/2/11
 */
public interface IMessageSenderQosHandler extends AfterConfigInitialize {

    String CHECK_INTER_VAL_KEY = "IMessageSenderQosHandler.checkInterval";

    String MESSAGES_VALID_TIME_KEY = "IMessageSenderQosHandler.messagesValidTime";

    String SCAN_RECEIVE_KEY = "IMessageSenderQosHandler.scanReceive";

    String SCHEDULED_EXECUTOR_SERVICE = "IMessageSenderQosHandler.scheduledExecutorService";

    /**
     * 处处理消息
     *
     * @param messageWrap 消息包装类
     * @param target      接收者
     */
    void handle(MessageWrap messageWrap, IMSession target);

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
