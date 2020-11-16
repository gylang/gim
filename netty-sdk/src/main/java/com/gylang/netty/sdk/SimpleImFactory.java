package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.message.MessageNotify;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.repo.IRepository;

/**
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public class SimpleImFactory implements AbstractImFactory {

    private NotifyProvider messagePusher;
    private NettyConfig nettyConfig;
    private IMRequestAdapter requestAdapter;
    private MessageNotify<Object> messageNotify;
    private IMServer imServer;



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


    public NotifyProvider getMessagePusher() {
        return messagePusher;
    }

    public void setMessagePusher(NotifyProvider messagePusher) {
        this.messagePusher = messagePusher;
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

    public MessageNotify<Object> getMessageNotify() {
        return messageNotify;
    }

    public void setMessageNotify(MessageNotify<Object> messageNotify) {
        this.messageNotify = messageNotify;
    }

}
