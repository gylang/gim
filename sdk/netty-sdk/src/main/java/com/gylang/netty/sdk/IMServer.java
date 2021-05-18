package com.gylang.netty.sdk;

import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.api.constant.GimDefaultConfigEnum;
import com.gylang.netty.sdk.api.initializer.CustomInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * im服务启动器
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@Slf4j
public class IMServer {

    @Setter
    private GimGlobalConfiguration nettyConfig;


    public void start() throws InterruptedException {


        // 连接线程组 工作线程组
        for (CustomInitializer<?> customInitializer : nettyConfig.getServerChannelInitializer()) {

            EventLoopGroup workerGroup =
                    new NioEventLoopGroup((Integer) GimDefaultConfigEnum.WORKER_GROUP.getValue(nettyConfig.getProperties()));
            EventLoopGroup bossGroup =
                    new NioEventLoopGroup((Integer) GimDefaultConfigEnum.BOSS_GROUP.getValue(nettyConfig.getProperties()));
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 三部曲启动 handler initializer bootstrap
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler((LogLevel) nettyConfig.getProperties(GimDefaultConfigEnum.LOG_LEVEL)))
                    .childHandler(customInitializer);

            ChannelFuture websocket = serverBootstrap
                    .bind(customInitializer.getPort())
                    .sync();
            // 监听服务启动
            websocket.syncUninterruptibly().channel().newSucceededFuture().addListener(f -> {
                log.info("==================================================");
                log.info("================{}:启动成功=====================",  customInitializer.getName());
                log.info("==================端口:{}=======================", customInitializer.getPort());
                log.info("==================================================");
            });
            // 监听服务关闭
            websocket.channel().closeFuture().addListener(future -> {
                log.info("websocket服务关闭");
                this.destroy(bossGroup, workerGroup);
            });

        }

    }

    /**
     * 停止netty服务
     */
    public void destroy(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        if (bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown()) {
            try {
                bossGroup.shutdownGracefully();
            } catch (Exception ignore) {
            }
        }
        if (workerGroup != null && !workerGroup.isShuttingDown() && !workerGroup.isShutdown()) {
            try {
                workerGroup.shutdownGracefully();
            } catch (Exception ignore) {
            }
        }
    }

}
