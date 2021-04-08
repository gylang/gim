package com.gylang.gim.web.im.intercept;

import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.web.dto.msg.ResponseMessageWrap;
import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.config.NettyConfiguration;
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
        return SystemChatCmd.QOS_RECEIVE_ACK != message.getCmd() && SystemChatCmd.QOS_SEND_ACK != message.getCmd();
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
