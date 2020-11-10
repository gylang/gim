package com.gylang.netty.sdk.call;

import com.gylang.netty.sdk.call.message.IdleNotify;
import com.gylang.netty.sdk.call.message.MessageNotify;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import static com.gylang.netty.sdk.constant.NettyNotifyConst.*;

/**
 * 心跳检测消息通知
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
public class SimpleHeartNotifyDispatch implements IdleNotify, MessageNotify<ChannelHandlerContext> {

    /**
     * 心跳监听实例
     */
    private final List<IdleNotify> idleNotifyList;

    public SimpleHeartNotifyDispatch(List<IdleNotify> idleNotifyList) {
        this.idleNotifyList = idleNotifyList;
    }


    /**
     * 读空闲通知
     *
     * @param ctx channel上下文
     */
    @Override
    public void readerIdle(ChannelHandlerContext ctx) {
        for (IdleNotify idleNotify : idleNotifyList) {
            idleNotify.readerIdle(ctx);
        }
    }

    /**
     * 写空闲通知
     *
     * @param ctx channel上下文
     */
    @Override
    public void writerIdle(ChannelHandlerContext ctx) {
        for (IdleNotify idleNotify : idleNotifyList) {
            idleNotify.writerIdle(ctx);
        }
    }

    /**
     * 读写空闲通知
     *
     * @param ctx channel上下文
     */
    @Override
    public void allIdle(ChannelHandlerContext ctx) {

        for (IdleNotify idleNotify : idleNotifyList) {
            idleNotify.allIdle(ctx);
        }
    }


    @Override
    @CallMessage({ALL_IDLE, READER_IDLE, WRITER_IDLE})
    public void msgNotify(String key, ChannelHandlerContext message) {
        if (ALL_IDLE.equals(key)) {
            allIdle(message);
        } else if (READER_IDLE.equals(key)) {
            readerIdle(message);
        } else if (WRITER_IDLE.equals(key)) {
            writerIdle(message);
        }
    }
}
