package com.gylang.netty.sdk.initializer;

import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.handler.netty.JsonDispatchHandler;
import com.gylang.netty.sdk.handler.netty.HeartCheckHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * netty 初始化器 当使用json进行交互时使用当前初始话器
 *
 * @author gylang
 * data 2020/11/6
 * @version v0.0.1
 */
public class JsonInitializer extends ChannelInitializer<SocketChannel> {

    private final Properties properties;

    private final NotifyProvider messagePusher;

    private final IMRequestAdapter requestAdapter;

    public JsonInitializer(Properties properties, NotifyProvider messagePusher, IMRequestAdapter requestAdapter) {
        this.properties = properties;
        this.messagePusher = messagePusher;
        this.requestAdapter = requestAdapter;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //空闲检测处理器
        // 参数 (设置时间内没有读操作(接收客户端数据) ,
        // 写时间(设置时间内没有给客户端写入数据),
        // all时间(设置时间内没有 读操作 or 写操作))
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(
                NettyConfigEnum.READER_IDLE.getValue(properties),
                NettyConfigEnum.WRITE_IDLE.getValue(properties),
                NettyConfigEnum.ALL_IDLE.getValue(properties),
                TimeUnit.SECONDS));
        //netty链式处理
        // 字符串 解码
        pipeline.addLast("DelimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
        pipeline.addLast("StringDecoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("StringEncoder", new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast("heart", new HeartCheckHandler(messagePusher, properties));
        pipeline.addLast("dispatch", new JsonDispatchHandler(requestAdapter, messagePusher));
    }
}

