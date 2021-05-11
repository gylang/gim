package com.gylang.netty.sdk.handler.netty;

import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.constant.GimDefaultConfigEnum;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.event.EventProvider;
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

    private final EventProvider eventProvider;
    private final Map<String, Object> properties;

    /**
     * 重连次数
     */
    private int unRecPingTimes;

    public HeartCheckHandler(GimGlobalConfiguration gimGlobalConfiguration) {
        this.eventProvider = gimGlobalConfiguration.getEventProvider();
        this.properties = gimGlobalConfiguration.getProperties();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;

            switch (event.state()) {
                case READER_IDLE:
                    eventProvider.sendEvent(EventTypeConst.READER_IDLE, ctx);
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventProvider.sendEvent(EventTypeConst.WRITER_IDLE, ctx);
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    eventProvider.sendEvent(EventTypeConst.ALL_IDLE, ctx);

                    break;
                default:
                    eventType = "";
            }
            log.info("客户触发心跳事件: {}", eventType);
            int retry = GimDefaultConfigEnum.LOST_CONNECT_RETRY_NUM.getValue(properties);
            if (ALL_IDLE.equals(event.state())) {
                if (unRecPingTimes >= retry) {
                    // 连续超过N次未收到client的ping消息，那么关闭该通道，等待client重连
                    eventProvider.sendAsyncEvent(EventTypeConst.OVER_TIME_CLOSE, new GIMSession(ctx.channel()));
                    // 一分钟为重连 断开连接
                    ctx.channel().close();
                } else {
                    // 失败计数器加1
                    unRecPingTimes++;
                }
            }

        }
    }


    /**
     * 在服务器端不使用心跳检测的情况下，如果客户端突然拔掉网线断网（注意这里不是客户度程序关闭，而仅是异常断网）
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            log.info("server " + ctx.channel().remoteAddress() + "关闭连接");
        }
        super.exceptionCaught(ctx, cause);
    }


}
