package com.gylang.netty.sdk;

import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
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
    private NettyConfiguration nettyConfig;


    public void start() throws InterruptedException {


        // 连接线程组 工作线程组
        EventLoopGroup workerGroup =
                new NioEventLoopGroup((Integer) NettyConfigEnum.WORKER_GROUP.getValue(nettyConfig.getProperties()));
        EventLoopGroup bossGroup =
                new NioEventLoopGroup((Integer) NettyConfigEnum.BOSS_GROUP.getValue(nettyConfig.getProperties()));
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 三部曲启动 handler initializer bootstrap
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler((LogLevel) nettyConfig.getProperties(NettyConfigEnum.LOG_LEVEL)))
                .childHandler(nettyConfig.getServerChannelInitializer());

        ChannelFuture channelFuture = serverBootstrap
                .bind((int) nettyConfig.getProperties(NettyConfigEnum.SOCKET_SERVER_PORT))
                .sync();
        // 监听服务启动
        channelFuture.syncUninterruptibly().channel().newSucceededFuture().addListener(f -> {
            log.info("==================================================");
            log.info("================netty启动成功=====================");
            log.info("==================端口:{}=======================",
                    (Object) nettyConfig.getProperties(NettyConfigEnum.SOCKET_SERVER_PORT));
            log.info("==================================================");
        });
        // 监听服务关闭
        channelFuture.channel().closeFuture().addListener(future -> {
            log.info("netty服务关闭");
            this.destroy(bossGroup, workerGroup);
        });
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
