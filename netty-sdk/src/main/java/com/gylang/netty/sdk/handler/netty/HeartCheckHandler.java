package com.gylang.netty.sdk.handler.netty;

import com.gylang.netty.sdk.call.MessagePusher;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import com.gylang.netty.sdk.constant.NettyNotifyConst;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Properties;

/**
 * 心跳监控 发送消息 用户业务对心跳检测和业务处理
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@ChannelHandler.Sharable
public class HeartCheckHandler extends ChannelInboundHandlerAdapter {

    private final MessagePusher messagePusher;
    private final Properties properties;
    /**
     * 重连次数
     */
    private int unRecPingTimes;

    public HeartCheckHandler(MessagePusher messagePusher, Properties properties) {
        this.messagePusher = messagePusher;
        this.properties = properties;
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
            System.out.println("eventType = " + eventType);
            int retry = NettyConfigEnum.LOST_CONNECT_RETRY_NUM.getValue(properties);
            if (unRecPingTimes >= retry) {
                // 连续超过N次未收到client的ping消息，那么关闭该通道，等待client重连
                ctx.channel().close();
            } else {
                // 失败计数器加1
                unRecPingTimes++;
            }
        }
    }

}
