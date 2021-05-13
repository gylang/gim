package com.gylang.netty.sdk.coder;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.domain.common.MessageWrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * websocket 协议拼装
 *
 * @author gylang
 * data 2020/11/17
 */
public class WebJsonMessageEncoder extends MessageToMessageEncoder<MessageWrap> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageWrap messageWrap, List<Object> list) throws Exception {

        // 判断协议类型
        list.add(new TextWebSocketFrame(JSON.toJSONString(messageWrap)));
    }
}
