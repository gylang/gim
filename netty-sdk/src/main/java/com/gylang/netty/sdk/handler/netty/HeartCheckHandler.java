package com.gylang.netty.sdk.handler.netty;

import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import com.gylang.netty.sdk.constant.NettyNotifyConst;
import com.gylang.netty.sdk.domain.model.IMSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

import static io.netty.handler.timeout.IdleState.ALL_IDLE;

/**
 * 心跳监控 发送消息 用户业务对心跳检测和业务处理
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@ChannelHandler.Sharable
@Slf4j
public class HeartCheckHandler extends ChannelInboundHandlerAdapter {

    private final NotifyProvider messagePusher;
    private final Map<String, Object> properties;
    /**
     * 重连次数
     */
    private int unRecPingTimes;

    public HeartCheckHandler(NotifyProvider messagePusher, Map<String, Object> properties) {
        this.messagePusher = messagePusher;
        this.properties = properties;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;

            switch (event.state()) {
                case READER_IDLE:
                    messagePusher.sendMsg(NettyNotifyConst.READER_IDLE, ctx);
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    messagePusher.sendMsg(NettyNotifyConst.WRITER_IDLE, ctx);
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    messagePusher.sendMsg(NettyNotifyConst.ALL_IDLE, ctx);

                    break;
                default:
                    eventType = "";
            }
            log.info("客户触发心跳事件: {}", eventType);
            int retry = NettyConfigEnum.LOST_CONNECT_RETRY_NUM.getValue(properties);
            if (ALL_IDLE.equals(event.state())) {
                if (unRecPingTimes >= retry) {
                    // 连续超过N次未收到client的ping消息，那么关闭该通道，等待client重连
                    ctx.channel().close();
                    // 一分钟为重连 断开连接
                    messagePusher.sendAsyncMsg(NettyNotifyConst.OVER_TIME_CLOSE, new IMSession(ctx.channel()));
                } else {
                    // 失败计数器加1
                    unRecPingTimes++;
                }
            }

        }
    }

//    /**
//     * channelInactive 被触发一定是和服务器断开了。分两种情况。一种是服务端close，一种是客户端close。
//     */
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
//        log.error("客户端与服务端断开连接,断开的时间为：" + (new Date()).toString());
//        // 定时线程 断线重连
//        final EventLoop eventLoop = ctx.channel().eventLoop();
//
//    }

    /**
     * 在服务器端不使用心跳检测的情况下，如果客户端突然拔掉网线断网（注意这里不是客户度程序关闭，而仅是异常断网）
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            System.out.println("server " + ctx.channel().remoteAddress() + "关闭连接");
        }
    }

}
