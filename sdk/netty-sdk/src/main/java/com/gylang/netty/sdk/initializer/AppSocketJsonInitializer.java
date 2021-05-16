package com.gylang.netty.sdk.initializer;

import com.gylang.netty.sdk.api.initializer.CustomInitializer;
import com.gylang.netty.sdk.coder.AppMessageDecoder;
import com.gylang.netty.sdk.coder.AppMessageEncoder;
import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.api.constant.GimDefaultConfigEnum;
import com.gylang.netty.sdk.api.handler.IMessageRouter;
import com.gylang.netty.sdk.handler.netty.HeartCheckHandler;
import com.gylang.netty.sdk.handler.netty.JsonDispatchHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * netty 初始化器 当使用json进行交互时使用当前初始话器
 *
 * @author gylang
 * data 2020/11/6
 * @version v0.0.1
 */
public class AppSocketJsonInitializer extends CustomInitializer<SocketChannel> {

    private Map<String, Object> properties;

    private GimGlobalConfiguration gimGlobalConfiguration;


    private IMessageRouter iMessageRouter;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //空闲检测处理器
        // 参数 (设置时间内没有读操作(接收客户端数据) ,
        // 写时间(设置时间内没有给客户端写入数据),
        // all时间(设置时间内没有 读操作 or 写操作))
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(
                GimDefaultConfigEnum.READER_IDLE.getValue(properties),
                GimDefaultConfigEnum.WRITE_IDLE.getValue(properties),
                GimDefaultConfigEnum.ALL_IDLE.getValue(properties),
                TimeUnit.SECONDS));
        //netty链式处理
        // 字符串 解码
        pipeline.addLast(new AppMessageEncoder());
        pipeline.addLast(new AppMessageDecoder());
        pipeline.addLast("heart", new HeartCheckHandler(gimGlobalConfiguration));
        pipeline.addLast("dispatch", new JsonDispatchHandler(iMessageRouter));
    }

    @Override
    public void init(GimGlobalConfiguration configuration) {
        this.gimGlobalConfiguration = configuration;
        this.properties = configuration.getProperties();
        this.iMessageRouter = configuration.getIMessageRouter();
    }

    @Override
    public String getName() {
        return "serverSocket";
    }
}

