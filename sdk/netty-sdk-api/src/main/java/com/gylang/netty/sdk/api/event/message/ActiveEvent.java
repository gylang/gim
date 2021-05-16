package com.gylang.netty.sdk.api.event.message;

import io.netty.channel.ChannelHandlerContext;

/**
 * 用户状态变更事件通知
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
public interface ActiveEvent {

    /**
     * 上线通知
     *
     * @param ctx 上下文
     */
    void onLineEvent(ChannelHandlerContext ctx);

    /**
     * 下线通知
     *
     * @param ctx 上下文
     */
    void onClose(ChannelHandlerContext ctx);

    /**
     * 退出通知
     *
     * @param ctx 上下文
     */
    void onLost(ChannelHandlerContext ctx);

}
