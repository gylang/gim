package com.gylang.spring.netty;

import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.*;
import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author gylang
 * data 2020/12/2
 */
@Configuration
@Slf4j
public class StartNettyServer implements InitializingBean {

    @Autowired
    private MessageProvider messageProvider;
    @Autowired
    private EventProvider eventProvider;
    @Autowired
    private EventContext eventContext;
    @Autowired
    private IMSessionRepository imSessionRepository;
    @Autowired
    private IMGroupSessionRepository imGroupSessionRepository;
    @Autowired(required = false)
    private NettyUserInfoFillHandler nettyUserInfoFillHandler;
    @Autowired
    private CustomInitializer<?> customInitializer;
    @Autowired
    private NettyConfig nettyConfig;
    @Autowired
    private DispatchAdapterHandler dispatchAdapterHandler;
    @Autowired
    private DataConverter dataConverter;

    @Autowired
    private DefaultRequestHandlerAdapter defaultRequestHandlerAdapter;
    @Autowired
    private DefaultNettyControllerAdapter defaultNettyControllerAdapter;
    @Autowired(required = false)
    private List<IMRequestHandler> imRequestHandlerList;
    @Autowired
    private List<NettyController<?>> nettyControllerList;
    @Autowired
    private List<BizRequestAdapter> bizRequestAdapterList;
    @Override
    public void afterPropertiesSet() throws Exception {




        log.info("初始化基础配置完成 : StartNettyServer");
    }
}
