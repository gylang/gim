package com.gylang.netty.sdk.coder;

import com.alibaba.fastjson.JSON;
import com.gylang.netty.sdk.domain.MessageWrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket 解码器
 *
 * @author gylang
 * data 2020/11/17
 */
public class WebJsonMessageDecoder extends SimpleChannelInboundHandler<Object> {

    private static final String URI = "ws://localhost:%d";

    private static final ConcurrentHashMap<String, WebSocketServerHandshaker> handShakerMap = new ConcurrentHashMap<>();

    /**
     * 接收到websocket协议
     *
     * @param ctx 上下文
     * @param msg 消息体
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // http请求
        if (msg instanceof FullHttpRequest) {
            handleHandshakeRequest(ctx, (FullHttpRequest) msg);
        }
        // websocket消息
        if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }


    }

    /**
     * websocket握手
     *
     * @param ctx 上下文
     * @param req 请求对象
     */
    private void handleHandshakeRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

        int port = ((InetSocketAddress) ctx.channel().localAddress()).getPort();

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(String.format(URI, port), null, false);

        WebSocketServerHandshaker handShaker = wsFactory.newHandshaker(req);

        handShakerMap.put(ctx.channel().id().asLongText(), handShaker);

        handShaker.handshake(ctx.channel(), req);
    }

    /**
     * 处理websocket消息
     *
     * @param ctx   上下文
     * @param frame websocket消息体
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        if (frame instanceof CloseWebSocketFrame) {
            // 关闭websocket
            handlerCloseWebSocketFrame(ctx, (CloseWebSocketFrame) frame);
            return;
        }

        if (frame instanceof PingWebSocketFrame) {
            // ping
            handlerPingWebSocketFrame(ctx, (PingWebSocketFrame) frame);
        } else if (frame instanceof PongWebSocketFrame) {
            // pong
            handlerPongWebSocketFrame(ctx, (PongWebSocketFrame) frame);
        } else if (frame instanceof TextWebSocketFrame) {
            //text消息
            handlerTextWebSocketFrame(ctx, (TextWebSocketFrame) frame);
        } else if (frame instanceof BinaryWebSocketFrame) {
            // byte消息格式
            handlerBinaryWebSocketFrame(ctx, (BinaryWebSocketFrame) frame);
        }

    }

    /**
     * 处理ping指令
     *
     * @param ctx   上下文
     * @param frame 消息体
     */
    private void handlerPongWebSocketFrame(ChannelHandlerContext ctx, PongWebSocketFrame frame) {
        ctx.channel().write(new PingWebSocketFrame(frame.content().retain()));
    }

    /**
     * 处理text消息体
     *
     * @param ctx   上下文
     * @param frame 消息体
     */
    private void handlerTextWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame) {

        MessageWrap object = JSON.parseObject(frame.text(), MessageWrap.class);
        ctx.fireChannelRead(object);
    }

    /**
     * 处理socket关闭
     *
     * @param ctx   上下文
     * @param frame 消息体
     */
    private void handlerCloseWebSocketFrame(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
        WebSocketServerHandshaker handShaker = handShakerMap.get(ctx.channel().id().asLongText());
        handShaker.close(ctx.channel(), frame.retain());
        handShakerMap.remove(ctx.channel().id().asLongText());
    }

    /**
     * 处理ping
     *
     * @param ctx   上下文
     * @param frame 消息体
     */
    private void handlerPingWebSocketFrame(ChannelHandlerContext ctx, PingWebSocketFrame frame) {
        ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
    }

    /**
     * 处理byte消息
     *
     * @param ctx   上下文
     * @param frame 消息体
     */
    private void handlerBinaryWebSocketFrame(ChannelHandlerContext ctx, BinaryWebSocketFrame frame) {
        byte[] data = new byte[frame.content().readableBytes()];
        frame.content().readBytes(data);

        MessageWrap object = JSON.parseObject(data, MessageWrap.class);
        ctx.fireChannelRead(object);
    }
}
