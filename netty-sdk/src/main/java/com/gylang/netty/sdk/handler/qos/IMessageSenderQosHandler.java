package com.gylang.netty.sdk.handler.qos;

import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;

/**
 * 处理发出消息是否已经安全送达，实现消息的重发
 * 当消息间隔内未收到ack，消息重复
 * @author gylang
 * data 2021/2/11
 */
public interface IMessageSenderQosHandler {
    /**
     * 处理消息
     * @param messageWrap 消息包装类
     * @param session 会话
     */
    void handle(MessageWrap messageWrap, IMSession session);
}
