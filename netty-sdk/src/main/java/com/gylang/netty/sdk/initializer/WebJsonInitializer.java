package com.gylang.netty.sdk.initializer;

import com.gylang.netty.sdk.coder.WebJsonMessageDecoder;
import com.gylang.netty.sdk.coder.WebJsonMessageEncoder;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.DispatchAdapterHandler;
import com.gylang.netty.sdk.handler.netty.HeartCheckHandler;
import com.gylang.netty.sdk.handler.netty.JsonDispatchHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * netty 初始化器 当使用json进行交互时使用当前初始话器
 *
 * @author gylang
 * data 2020/11/6
 * @version v0.0.1
 */
public class WebJsonInitializer extends CustomInitializer<SocketChannel> {

    private  Map<String, Object> properties;

    private  NettyConfiguration nettyConfiguration;

    private  EventProvider eventProvider;

    private  DispatchAdapterHandler dispatchAdapterHandler;




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
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebJsonMessageDecoder());
        pipeline.addLast(new WebJsonMessageEncoder());
//        pipeline.addLast("DelimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
        pipeline.addLast("StringDecoder", new StringDecoder(CharsetUtil.UTF_8));
//        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
//        pipeline.addLast("StringEncoder", new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast("heart", new HeartCheckHandler(nettyConfiguration));
        pipeline.addLast("dispatch", new JsonDispatchHandler(dispatchAdapterHandler, eventProvider));
    }

    @Override
    public void init(NettyConfiguration configuration) {
        this.nettyConfiguration = configuration;
        this.properties = configuration.getProperties();
        this.eventProvider = configuration.getEventProvider();
        this.dispatchAdapterHandler = configuration.getDispatchAdapterHandler();
    }
}

