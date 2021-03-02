package com.gylang.netty.sdk.provider;

import com.gylang.netty.sdk.common.Initializer;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;

/**
 * @author gylang
 * data 2020/11/12
 * @version v0.0.1
 */
public interface MessageProvider extends Initializer {

    /**
     * 发送消息
     * @param me 发生者
     * @param target 发送目标
     * @param message 发送消息
     */
    void sendMsg(IMSession me, String target, MessageWrap message);

    /**
     * 发送消息 异步
     * @param me 发生者
     * @param target 发送目标
     * @param message 发送消息
     */
    void sendAsyncMsg(IMSession me, String target, MessageWrap message);

    /**
     * 群送消息
     * @param me 发生者
     * @param target 发送目标
     * @param message 发送消息
     */
    void sendGroup(IMSession me, String target, MessageWrap message);

    /**
     * 群发消息 异步
     * @param me 发生者
     * @param target 发送目标
     * @param message 发送消息
     */
    void sendAsyncGroup(IMSession me, String target, MessageWrap message);

/**
     * 发送消息
     * @param me 发生者
     * @param target 发送目标
     * @param message 发送消息
     */
    void sendMsg(IMSession me, IMSession target, MessageWrap message);

    /**
     * 发送消息 异步
     * @param me 发生者
     * @param target 发送目标
     * @param message 发送消息
     */
    void sendAsyncMsg(IMSession me, IMSession target, MessageWrap message);

    /**
     * 群送消息
     * @param me 发生者
     * @param target 发送目标
     * @param message 发送消息
     */
    void sendGroup(IMSession me, AbstractSessionGroup target, MessageWrap message);

    /**
     * 群发消息 异步
     * @param me 发生者
     * @param target 发送目标
     * @param message 发送消息
     */
    void sendAsyncGroup(IMSession me, AbstractSessionGroup target, MessageWrap message);


}
