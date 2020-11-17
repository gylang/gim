package com.gylang.netty.sdk.config;

import com.gylang.netty.sdk.constant.NettyConfigEnum;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * netty服务配置 方便定制化服务
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@Data
public class NettyConfig {

    /**
     * 负责接收连接，为每一个连接创建从线程，不仅接收也处理；
     */
    private NioEventLoopGroup bossGroup;
    /**
     * 负责接收许多客户端的读写操作，为每个请求创建处理线程，不仅接收也处理；
     */
    private NioEventLoopGroup workerGroup;


    /**
     * 根据名称装配，防止和客户端的ChannelInitializer冲突报错
     */
    private ChannelInitializer<?> serverChannelInitializer;
    /**
     * 配置信息
     */
    @Getter
    private Properties properties;

    /**
     * 用来监控tcp链接 指定线程数 默认是1 用默认即可
     */
    public NioEventLoopGroup bossGroup() {
        if (null == this.bossGroup) {
            this.bossGroup = new NioEventLoopGroup((Integer) NettyConfigEnum.BOSS_GROUP.getValue(properties));
        }
        return this.bossGroup;
    }

    /**
     * 处理io事件 一定要多线程效率高 源码中默认是cpu核数*2
     */
    public NioEventLoopGroup workerGroup() {
        if (null == this.workerGroup) {
            this.workerGroup = new NioEventLoopGroup((Integer) NettyConfigEnum.WORKER_GROUP.getValue(properties));
        }
        return this.workerGroup;
    }

    /**
     * 获取配置值
     *
     * @param key key
     * @return 配置值
     */
    public <T> T getProperties(String key) {
        return NettyConfigEnum.getValue(key, properties);
    }

    /**
     * 获取配置值
     *
     * @param key key
     * @return 配置值
     */
    public <T> T getProperties(NettyConfigEnum key) {
        return key.getValue(properties);
    }

    /**
     * netty服务器相关设置
     */
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<>();

        /*是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）
        并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活
        */
//        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
         /*
         BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
        用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，
        Java将使用默认值50
        */
//        options.put(ChannelOption.SO_BACKLOG, backlog);

        /*
        在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，
        同时，对方接收到数据，也需要发送ACK表示确认。
        为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。
        这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
        */
        options.put(ChannelOption.TCP_NODELAY, true);

        return options;
    }

}
