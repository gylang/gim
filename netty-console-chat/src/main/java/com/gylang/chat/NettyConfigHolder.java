package com.gylang.chat;

import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.config.SimpleNettyConfigurationInitializer;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.conveter.JsonConverter;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.event.DefaultEventContext;
import com.gylang.netty.sdk.event.DefaultEventProvider;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.handler.*;
import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.initializer.WebJsonInitializer;
import com.gylang.netty.sdk.provider.DefaultMessageProvider;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import com.gylang.netty.sdk.util.ObjectWrapUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
@Slf4j
public class NettyConfigHolder {

    private static NettyConfiguration nettyConfiguration = new NettyConfiguration();

    public static NettyConfiguration getInstance() {
        return nettyConfiguration;
    }

    public static void init() {

        nettyConfiguration.setServerChannelInitializer(new WebJsonInitializer());
        nettyConfiguration.setEventProvider(new DefaultEventProvider());
        nettyConfiguration.setEventContext(new EventContext());
        nettyConfiguration.setDataConverter(new JsonConverter());
        nettyConfiguration.setSessionRepository(new DefaultIMRepository());
        nettyConfiguration.setGroupSessionRepository(new DefaultGroupRepository());
        nettyConfiguration.setMessageProvider(new DefaultMessageProvider());
        nettyConfiguration.setMessageEventListener(new ArrayList<>());
        List<BizRequestAdapter<?>> bizRequestAdapterList = new ArrayList<>();
        bizRequestAdapterList.add(new DefaultNettyControllerAdapter());
        bizRequestAdapterList.add(new DefaultRequestHandlerAdapter());
        nettyConfiguration.setBizRequestAdapterList(bizRequestAdapterList);
        nettyConfiguration.setDispatchAdapterHandler(new DefaultAdapterDispatch());
        nettyConfiguration.setNettyInterceptList(new ArrayList<>());


    }
}
