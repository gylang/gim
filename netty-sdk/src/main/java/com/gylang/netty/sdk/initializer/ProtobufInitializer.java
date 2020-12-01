package com.gylang.netty.sdk.initializer;

import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import com.gylang.netty.sdk.domain.proto.MessageWrapProto;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.handler.netty.HeartCheckHandler;
import com.gylang.netty.sdk.handler.netty.ProtobufDispatchHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */

public class ProtobufInitializer extends CustomInitializer<SocketChannel> {

    private final Map<String, Object> properties;

    private final NotifyProvider pusher;
    private final IMRequestAdapter requestAdapter;

    public ProtobufInitializer(Map<String, Object> properties, NotifyProvider pusher, IMRequestAdapter requestAdapter) {
        this.properties = properties;
        this.pusher = pusher;
        this.requestAdapter = requestAdapter;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //空闲检测 心跳检测
        // 参数 (设置时间内没有读操作(接收客户端数据) ,
        // 写时间(设置时间内没有给客户端写入数据),
        // all时间(设置时间内没有 读操作 or 写操作))
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(
                NettyConfigEnum.READER_IDLE.getValue(properties),
                NettyConfigEnum.WRITE_IDLE.getValue(properties),
                NettyConfigEnum.ALL_IDLE.getValue(properties),
                TimeUnit.SECONDS));
        pipeline.addLast("heart", new HeartCheckHandler(pusher, properties));
        //netty链式处理
        // protobuf 解码
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(MessageWrapProto.Model.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        // 业务分发
        pipeline.addLast("dispatch", new ProtobufDispatchHandler(requestAdapter, pusher));
    }
}
