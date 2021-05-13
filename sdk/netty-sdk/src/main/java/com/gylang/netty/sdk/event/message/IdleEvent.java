package com.gylang.netty.sdk.event.message;

import io.netty.channel.ChannelHandlerContext;

/**
 * 空闲等待事件通知
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
public interface IdleEvent {

    /**
     * 读空闲通知
     *
     * @param ctx channel上下文
     */
    void readerIdle(ChannelHandlerContext ctx);

    /**
     * 写空闲通知
     *
     * @param ctx channel上下文
     */
    void writerIdle(ChannelHandlerContext ctx);

    /**
     * 读写空闲通知
     *
     * @param ctx channel上下文
     */
    void allIdle(ChannelHandlerContext ctx);
}
