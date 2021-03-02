package com.gylang.netty.sdk.handler.netty;

import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.DispatchAdapterHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty json数据协议 服务分发
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
public class JsonDispatchHandler extends SimpleChannelInboundHandler<MessageWrap> {

    private final DispatchAdapterHandler dispatchAdapterHandler;
    private final EventProvider messagePusher;

    public JsonDispatchHandler(DispatchAdapterHandler bizDispatchHandler, EventProvider messagePusher) {
        this.dispatchAdapterHandler = bizDispatchHandler;
        this.messagePusher = messagePusher;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageWrap msg) {
        IMSession session = new IMSession(ctx.channel());
        if (null == msg || null == msg.getCmd()) {
            return;
        }
        dispatchAdapterHandler.process(ctx, session, msg);

//        bizDispatchHandler.process(bizDispatchHandler);
    }


}
