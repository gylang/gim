package com.gylang.netty.sdk.coder;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.domain.common.MessageWrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

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

        // 判断协议类型
        Channel channel = channelHandlerContext.channel();
        list.add(new TextWebSocketFrame(JSON.toJSONString(messageWrap)));
    }
}
