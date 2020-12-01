package com.gylang.netty.sdk.coder;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.gylang.netty.sdk.domain.MessageWrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket 解码器
 * @author gylang
 * data 2020/11/17
 */
public class WebJsonMessageDecoder extends SimpleChannelInboundHandler<Object> {

    private final static String URI = "ws://localhost:%d";

    private static final ConcurrentHashMap<String, WebSocketServerHandshaker> handShakerMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            handleHandshakeRequest(ctx, (FullHttpRequest) msg);
        }
        if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }



    }

    private void handleHandshakeRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

        int port = ((InetSocketAddress) ctx.channel().localAddress()).getPort();

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(String.format(URI, port), null, false);

        WebSocketServerHandshaker handShaker = wsFactory.newHandshaker(req);

        handShakerMap.put(ctx.channel().id().asLongText(), handShaker);

        handShaker.handshake(ctx.channel(), req);
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws InvalidProtocolBufferException {

        if (frame instanceof CloseWebSocketFrame) {
            handlerCloseWebSocketFrame(ctx, (CloseWebSocketFrame) frame);
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            handlerPingWebSocketFrame(ctx, (PingWebSocketFrame) frame);
            return;
        } else if (frame instanceof TextWebSocketFrame) {
            handlerTextWebSocketFrame(ctx, (TextWebSocketFrame) frame);
        }

        handlerBinaryWebSocketFrame(ctx, (BinaryWebSocketFrame) frame);
    }

    private void handlerTextWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame) {

        MessageWrap object = JSON.parseObject(frame.text(), MessageWrap.class);
        ctx.fireChannelRead(object);
    }

    private void handlerCloseWebSocketFrame(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
        WebSocketServerHandshaker handShaker = handShakerMap.get(ctx.channel().id().asLongText());
        handShaker.close(ctx.channel(), frame.retain());
    }

    private void handlerPingWebSocketFrame(ChannelHandlerContext ctx, PingWebSocketFrame frame) {
        ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
    }

    private void handlerBinaryWebSocketFrame(ChannelHandlerContext ctx, BinaryWebSocketFrame frame) throws InvalidProtocolBufferException {
        byte[] data = new byte[frame.content().readableBytes()];
        frame.content().readBytes(data);

        MessageWrap object = JSON.parseObject(data, MessageWrap.class);
        ctx.fireChannelRead(object);
    }
}
