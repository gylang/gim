package com.gylang.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<String> {


    /**
     * 客户端监听写事件。也就是设置时间内没有与服务端交互则发送ping 给服务端
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.ALL_IDLE) {
                ctx.writeAndFlush("PING");
                log.info("send PING");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * channelInactive 被触发一定是和服务器断开了。分两种情况。一种是服务端close，一种是客户端close。
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.error("客户端与服务端断开连接,断开的时间为：" + (new Date()).toString());
        // 定时线程 断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        //todo 设置断开连接后重连时间，此设置是断开连接一分钟（60秒）后重连

    }

    /**
     * 在服务器端不使用心跳检测的情况下，如果客户端突然拔掉网线断网（注意这里不是客户度程序关闭，而仅是异常断网）
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            System.out.println("server " + ctx.channel().remoteAddress() + "关闭连接");
        }
    }


    /**
     * 消息监控，监听服务端传来的消息(和netty版本有关，有的版本这个方法叫做clientRead0)
     */

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("监听到心跳包 {}", msg);
    }
}
