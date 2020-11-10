package com.gylang.netty.sdk.call.message;

import io.netty.channel.ChannelHandlerContext;

/**
 * 在线状态通知
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
public interface ActiveNotify {

    /**
     * 上线通知
     *
     * @param ctx 上下文
     */
    void register(ChannelHandlerContext ctx);

    /**
     * 下线通知
     *
     * @param ctx 上下文
     */
    void close(ChannelHandlerContext ctx);

    /**
     * 退出通知
     *
     * @param ctx 上下文
     */
    void lost(ChannelHandlerContext ctx);

}
