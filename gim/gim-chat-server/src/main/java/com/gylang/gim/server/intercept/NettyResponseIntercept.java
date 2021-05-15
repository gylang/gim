package com.gylang.gim.server.intercept;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.common.ResponseMessage;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.domain.model.GIMSession;
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
    public void init(GimGlobalConfiguration configuration) {
        this.messageProvider = configuration.getMessageProvider();
    }

    @Override
    public boolean support(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {
        return ChatType.QOS_CLIENT_SEND_ACK != message.getType() && ChatType.QOS_SERVER_SEND_ACK != message.getType();
    }

    @Override
    public boolean doBefore(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {
        return false;
    }

    @Override
    public Object doAfter(ChannelHandlerContext ctx, GIMSession me, MessageWrap message, Object result) {


        if (result instanceof ResponseMessage) {

            MessageWrap wrap = (MessageWrap) result;
            messageProvider.sendMsg(me, me, wrap);

        }
        return result;
    }
}
