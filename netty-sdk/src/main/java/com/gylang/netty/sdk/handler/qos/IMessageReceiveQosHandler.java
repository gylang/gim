package com.gylang.netty.sdk.handler.qos;

import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;

/**
 * 处理收到接受到的消息，发送ack，保证消息已经接受成功
 * 存储消息列表，当用户消息重复的时候对其进行消费判断，假设重发信息，回复ack
 * 当消息进行双向ack之后，清除消息
 * @author gylang
 * data 2021/2/11
 */
public interface IMessageReceiveQosHandler {
    /**
     * 处处理消息
     *
     * @param messageWrap 消息包装类
     * @param session     会话
     */
    void handle(MessageWrap messageWrap, IMSession session);

    /**
     * 是否已经接收到当前消息
     * @return 是否接收
     */
    boolean hasReceived();

    /**
     * 添加已接收队列
     * @param messageWrap
     */
    void addReceived(MessageWrap messageWrap);

    public void startup();

    void stop();

}
