package com.gylang.chat;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.conveter.JsonConverter;
import com.gylang.netty.sdk.event.DefaultEventProvider;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.DefaultMessageRouter;
import com.gylang.netty.sdk.handler.adapter.DefaultGimControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.handler.qos.DefaultGIMessageReceiveQosHandler;
import com.gylang.netty.sdk.handler.qos.DefaultGIMessageSendQosHandler;
import com.gylang.netty.sdk.initializer.WebSocketJsonInitializer;
import com.gylang.netty.sdk.provider.DefaultMessageProvider;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
@Slf4j
public class NettyConfigHolder {

    private static GimGlobalConfiguration gimGlobalConfiguration = new GimGlobalConfiguration();

    public static GimGlobalConfiguration getInstance() {
        return gimGlobalConfiguration;
    }

    public static void init() {

        gimGlobalConfiguration.setServerChannelInitializer(CollUtil.newArrayList(new WebSocketJsonInitializer()));
        gimGlobalConfiguration.setEventProvider(new DefaultEventProvider());
        gimGlobalConfiguration.setEventContext(new EventContext());
        gimGlobalConfiguration.setDataConverter(new JsonConverter());
        gimGlobalConfiguration.setSessionRepository(new DefaultIMRepository());
        gimGlobalConfiguration.setGroupSessionRepository(new DefaultGroupRepository());
        gimGlobalConfiguration.setMessageProvider(new DefaultMessageProvider());
        gimGlobalConfiguration.setMessageEventListener(new ArrayList<>());
        List<BizRequestAdapter> bizRequestAdapterList = new ArrayList<>();
        bizRequestAdapterList.add(new DefaultGimControllerAdapter());
        bizRequestAdapterList.add(new DefaultRequestHandlerAdapter());
        gimGlobalConfiguration.setBizRequestAdapterList(bizRequestAdapterList);
        gimGlobalConfiguration.setIMessageRouter(new DefaultMessageRouter());
        gimGlobalConfiguration.setNettyInterceptList(new ArrayList<>());
        gimGlobalConfiguration.setIMessageReceiveQosHandler(new DefaultGIMessageReceiveQosHandler());
        gimGlobalConfiguration.setIMessageSenderQosHandler(new DefaultGIMessageSendQosHandler());

    }
}
