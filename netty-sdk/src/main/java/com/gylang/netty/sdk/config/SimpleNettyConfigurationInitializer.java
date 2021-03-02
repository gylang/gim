package com.gylang.netty.sdk.config;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.initializer.CustomInitializer;

/**
 * @author gylang
 * data 2021/3/2
 */
public class SimpleNettyConfigurationInitializer {


    public void initConfig(NettyConfiguration configuration) {

        // 事件上下文
        EventContext eventContext = configuration.getEventContext();
        if (CollUtil.isNotEmpty(configuration.getMessageEventListener())) {
            for (MessageEventListener<?> messageEventListener : configuration.getMessageEventListener()) {
                eventContext.register(messageEventListener);
            }
        }

        // 消息发送
        configuration.getEventProvider().init(configuration);

        // 消息发送器
        configuration.getMessageProvider().init(configuration);
        // 消息业务适配器
        for (BizRequestAdapter<?> bizRequestAdapter : configuration.getBizRequestAdapterList()) {
            bizRequestAdapter.init(configuration);
        }
        CustomInitializer<?> serverChannelInitializer = configuration.getServerChannelInitializer();
        serverChannelInitializer.init(configuration);
        // 消息分发
        configuration.getDispatchAdapterHandler().init(configuration);
    }
}
