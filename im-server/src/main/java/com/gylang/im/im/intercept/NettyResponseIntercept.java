package com.gylang.im.im.intercept;

import com.gylang.im.web.dto.msg.ResponseMessageWrap;
import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.constant.MessageType;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.intercept.NettyIntercept;
import com.gylang.netty.sdk.provider.MessageProvider;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2021/3/6
 */
@Component
public class NettyResponseIntercept implements NettyIntercept, AfterConfigInitialize {

    private MessageProvider messageProvider;

    @Override
    public void init(NettyConfiguration configuration) {
        this.messageProvider = configuration.getMessageProvider();
    }

    @Override
    public boolean support(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {
        return MessageType.QOS_RECEIVE_ACK != message.getType() && MessageType.QOS_SEND_ACK != message.getType();
    }

    @Override
    public boolean doBefore(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {
        return false;
    }

    @Override
    public Object doAfter(ChannelHandlerContext ctx, IMSession me, MessageWrap message, Object result) {

        if (result instanceof ResponseMessageWrap) {

            messageProvider.sendMsg(me, me, (MessageWrap) result);
        }
        return result;
    }
}