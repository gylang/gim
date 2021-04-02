package com.gylang.gim.im.intercept;

import com.gylang.gim.im.domain.ResponseMessageWrap;
import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.constant.system.SystemMessageType;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.qos.IMessageReceiveQosHandler;
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

    private IMessageReceiveQosHandler receiveQosHandler;

    @Override
    public void init(NettyConfiguration configuration) {
        this.messageProvider = configuration.getMessageProvider();
        this.receiveQosHandler = configuration.getIMessageReceiveQosHandler();
    }

    @Override
    public boolean support(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {
        return !SystemMessageType.QOS_RECEIVE_ACK.equals(message.getCmd()) && !SystemMessageType.QOS_SEND_ACK.equals(message.getCmd());
    }

    @Override
    public boolean doBefore(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {
        return false;
    }

    @Override
    public Object doAfter(ChannelHandlerContext ctx, IMSession me, MessageWrap message, Object result) {


        if (result instanceof ResponseMessageWrap) {

            MessageWrap wrap = (MessageWrap) result;
            messageProvider.sendMsg(me, me, wrap);
            receiveQosHandler.handle(wrap, me);

        }
        return result;
    }
}
