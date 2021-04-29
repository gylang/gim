package com.gylang.netty.sdk.provider;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author gylang
 * data 2020/11/12
 * @version v0.0.1
 */
public interface MessageProvider extends AfterConfigInitialize {

    /**
     * 发送消息
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    int sendMsg(IMSession me, String target, MessageWrap message);

    /**
     * 发送消息 异步
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    int sendMsgCallBack(IMSession me, String target, MessageWrap message, GenericFutureListener<? extends Future<? super Void>> listener);

    /**
     * 发送消息
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    int sendMsg(IMSession me, IMSession target, MessageWrap message);

    /**
     * 发送消息 异步
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    int sendMsgCallBack(IMSession me, IMSession target, MessageWrap message, GenericFutureListener<? extends Future<? super Void>> listener);

    /**
     * 群送消息
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    int sendGroup(IMSession me, String target, MessageWrap message);

    /**
     * 群发消息 异步
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    void sendAsyncGroup(IMSession me, String target, MessageWrap message);




    /**
     * 群送消息
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    int sendGroup(IMSession me, AbstractSessionGroup target, MessageWrap message);

    /**
     * 群发消息 异步
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    void sendAsyncGroup(IMSession me, AbstractSessionGroup target, MessageWrap message);

    Integer SENDING = 1;

    Integer USER_NOT_FOUND = 2;

    Integer CROSS_SERVER = 3;
}
