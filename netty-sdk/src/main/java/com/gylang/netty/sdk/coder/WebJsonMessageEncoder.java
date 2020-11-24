package com.gylang.netty.sdk.coder;

import com.alibaba.fastjson.JSON;
import com.gylang.netty.sdk.domain.MessageWrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

/**
 *
 * @author gylang
 * data 2020/11/17
 */
public class WebJsonMessageEncoder extends MessageToMessageEncoder<MessageWrap> {

    private static final UnpooledByteBufAllocator BYTE_BUF_ALLOCATOR = new UnpooledByteBufAllocator(false);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageWrap messageWrap, List<Object> list) throws Exception {
        list.add(new TextWebSocketFrame(JSON.toJSONString(messageWrap)));
    }
}
