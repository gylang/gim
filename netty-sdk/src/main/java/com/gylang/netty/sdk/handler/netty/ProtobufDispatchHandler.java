package com.gylang.netty.sdk.handler.netty;

import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.domain.proto.MessageWrapProto;
import com.gylang.netty.sdk.handler.DispatchAdapterHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * netty 协议处理器 接收protobuf 数据处理 并实现请求向上分发
 */
@Slf4j
public class ProtobufDispatchHandler extends SimpleChannelInboundHandler<MessageWrapProto.Model> {
    private final DispatchAdapterHandler requestAdapter;

    public ProtobufDispatchHandler(NettyConfiguration nettyConfiguration) {
        this.requestAdapter = nettyConfiguration.getDispatchAdapterHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageWrapProto.Model msg) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("收到消息 : {}", msg);
        }
        IMSession session = new IMSession(ctx.channel());
        MessageWrap messageWrap = new MessageWrap();
        messageWrap.setCmd(msg.getKey());
        messageWrap.setContent(msg.getContent());
        messageWrap.setType(Byte.parseByte(msg.getType()));
        messageWrap.setReceiverType(msg.getReceiverType());
        requestAdapter.process(ctx, session, messageWrap);

    }
}
