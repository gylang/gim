package com.gylang.netty.sdk.handler.netty;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.netty.sdk.domain.model.GIMSession;
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

    private final IMessageRouter iMessageRouter;

    public JsonDispatchHandler(IMessageRouter bizDispatchHandler) {
        this.iMessageRouter = bizDispatchHandler;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageWrap msg) {
        GIMSession session = new GIMSession(ctx.channel());
        if (null == msg || ChatTypeEnum.HEART == msg.getType()) {

            return;
        }
        if (log.isDebugEnabled()) {
            log.info("[接收到客户端消息] : 用户id：{}，channelId：{}", session.getAccount(), session.getNid());
        }
        iMessageRouter.process(ctx, session, msg);

    }


}
