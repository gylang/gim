package com.gylang.netty.sdk.provider;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.GIMSession;
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
    int sendMsg(GIMSession me, String target, MessageWrap message);

    /**
     * 发送消息 异步
     *
     * @param me       发生者
     * @param target   发送目标
     * @param message  发送消息
     * @param listener 事件监听器
     * @return 发送结果标识
     */
    int sendMsgCallBack(GIMSession me, String target, MessageWrap message, GenericFutureListener<? extends Future<? super Void>> listener);

    /**
     * 发送消息
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     * @return 发送结果标识
     */
    int sendMsg(GIMSession me, GIMSession target, MessageWrap message);

    /**
     * 发送消息 异步
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     * @param listener 事件监听器
     * @return 发送结果标识
     */
    int sendMsgCallBack(GIMSession me, GIMSession target, MessageWrap message, GenericFutureListener<? extends Future<? super Void>> listener);

    /**
     * 群送消息
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     * @return 发送结果标识
     */
    int sendGroup(GIMSession me, String target, MessageWrap message);

    /**
     * 群发消息 异步
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    void sendAsyncGroup(GIMSession me, String target, MessageWrap message);


    /**
     * 群送消息
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    int sendGroup(GIMSession me, AbstractSessionGroup target, MessageWrap message);

    /**
     * 群发消息 异步
     *
     * @param me      发生者
     * @param target  发送目标
     * @param message 发送消息
     */
    void sendAsyncGroup(GIMSession me, AbstractSessionGroup target, MessageWrap message);
    /** 发送中 */
    Integer SENDING = 1;
    /** 用户未发现 */
    Integer USER_NOT_FOUND = 2;
    /** 跨服消息 */
    Integer CROSS_SERVER = 3;
}
