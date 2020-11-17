package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.message.MessageNotify;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.repo.IRepository;
import com.sun.istack.internal.NotNull;

/**
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public class SimpleImFactory implements AbstractImFactory {

    @NotNull
    private NotifyProvider notifyProvider;
    @NotNull
    private NettyConfig nettyConfig;
    @NotNull
    private IMRequestAdapter requestAdapter;
    @NotNull
    private IMServer imServer;
    @NotNull
    private IMContext imContext;


    @Override
    public void init() {
        // 启动netty服务
        imServer = new IMServer();
        imServer.setNettyConfig(nettyConfig);

    }

    @Override
    public void start() {
        try {
            imServer.init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IMContext imContext() {
        return this.imContext;
    }


    public NettyConfig getNettyConfig() {
        return nettyConfig;
    }

    public void setNettyConfig(NettyConfig nettyConfig) {
        this.nettyConfig = nettyConfig;
    }

    public IMRequestAdapter getRequestAdapter() {
        return requestAdapter;
    }

    public void setRequestAdapter(IMRequestAdapter requestAdapter) {
        this.requestAdapter = requestAdapter;
    }

    public NotifyProvider getNotifyProvider() {
        return notifyProvider;
    }

    public void setNotifyProvider(NotifyProvider notifyProvider) {
        this.notifyProvider = notifyProvider;
    }


    public IMServer getImServer() {
        return imServer;
    }

    public void setImServer(IMServer imServer) {
        this.imServer = imServer;
    }

    public void setImContext(IMContext imContext) {
        this.imContext = imContext;
    }
}
