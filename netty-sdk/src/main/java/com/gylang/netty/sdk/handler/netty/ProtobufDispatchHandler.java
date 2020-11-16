package com.gylang.netty.sdk.handler.netty;

import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.domain.proto.MessageWrapProto;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * netty 协议处理器 接收protobuf 数据处理 并实现请求向上分发
 */
@Slf4j
public class ProtobufDispatchHandler extends SimpleChannelInboundHandler<MessageWrapProto.Model> {
    private final IMRequestAdapter requestAdapter;
    private final NotifyProvider messagePusher;

    public ProtobufDispatchHandler(IMRequestAdapter requestAdapter, NotifyProvider messagePusher) {
        this.requestAdapter = requestAdapter;
        this.messagePusher = messagePusher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageWrapProto.Model msg) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("收到消息 : {}", msg);
        }
        IMSession session = new IMSession(ctx.channel());
        MessageWrap messageWrap = new MessageWrap();
        messageWrap.setKey(msg.getKey());
        messageWrap.setContent(msg.getContent());
        messageWrap.setType(msg.getType());
        messageWrap.setReceiverType(msg.getReceiverType());
        messageWrap.setBytes(msg.getBytes());
        requestAdapter.process(ctx, session, messageWrap, messagePusher);

    }
}
