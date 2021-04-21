package com.gylang.netty.sdk.handler.netty;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.IMessageRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * netty json数据协议 服务分发
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@Slf4j
public class JsonDispatchHandler extends SimpleChannelInboundHandler<MessageWrap> {

    private final IMessageRouter IMessageRouter;

    public JsonDispatchHandler(IMessageRouter bizDispatchHandler, EventProvider messagePusher) {
        this.IMessageRouter = bizDispatchHandler;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageWrap msg) {
        IMSession session = new IMSession(ctx.channel());
        if (null == msg || null == msg.getCmd()) {

            return;
        }
        if (log.isDebugEnabled()) {
            log.info("[接收到客户端消息] : 用户id：{}，channelId：{}", session.getAccount(), session.getNid());
        }
        IMessageRouter.process(ctx, session, msg);

    }


}
