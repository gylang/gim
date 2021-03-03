package com.gylang.netty.sdk.event;

import com.gylang.netty.sdk.event.message.IdleEvent;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import static com.gylang.netty.sdk.constant.EventTypeConst.*;

/**
 * 心跳检测消息通知
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
public class DefaultHeartHandler implements IdleEvent, MessageEventListener<ChannelHandlerContext> {

    /**
     * 心跳监听实例
     */
    private final List<IdleEvent> idleEventList;

    public DefaultHeartHandler(List<IdleEvent> idleEventList) {
        this.idleEventList = idleEventList;
    }


    /**
     * 读空闲通知
     *
     * @param ctx channel上下文
     */
    @Override
    public void readerIdle(ChannelHandlerContext ctx) {
        for (IdleEvent idleEvent : idleEventList) {
            idleEvent.readerIdle(ctx);
        }
    }

    /**
     * 写空闲通知
     *
     * @param ctx channel上下文
     */
    @Override
    public void writerIdle(ChannelHandlerContext ctx) {
        for (IdleEvent idleEvent : idleEventList) {
            idleEvent.writerIdle(ctx);
        }
    }

    /**
     * 读写空闲通知
     *
     * @param ctx channel上下文
     */
    @Override
    public void allIdle(ChannelHandlerContext ctx) {

        for (IdleEvent idleEvent : idleEventList) {
            idleEvent.allIdle(ctx);
        }
    }


    @Override
    @MessageEvent({ALL_IDLE, READER_IDLE, WRITER_IDLE})
    public void onEvent(String key, ChannelHandlerContext message) {
        if (ALL_IDLE.equals(key)) {
            allIdle(message);
        } else if (READER_IDLE.equals(key)) {
            readerIdle(message);
        } else if (WRITER_IDLE.equals(key)) {
            writerIdle(message);
        }
    }
}
