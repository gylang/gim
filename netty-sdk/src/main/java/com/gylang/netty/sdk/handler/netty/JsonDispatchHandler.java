package com.gylang.netty.sdk.handler.netty;

import com.alibaba.fastjson.JSON;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty json数据协议 服务分发
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
public class JsonDispatchHandler extends SimpleChannelInboundHandler<String> {

    private final IMRequestAdapter requestAdapter;
    private final NotifyProvider messagePusher;

    public JsonDispatchHandler(IMRequestAdapter bizDispatchHandler, NotifyProvider messagePusher) {
        this.requestAdapter = bizDispatchHandler;
        this.messagePusher = messagePusher;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        MessageWrap messageWrap = JSON.parseObject(msg, MessageWrap.class);
        IMSession session = new IMSession(ctx.channel());
        requestAdapter.process(ctx, session, messageWrap, messagePusher);

//        bizDispatchHandler.process(bizDispatchHandler);
    }
}
