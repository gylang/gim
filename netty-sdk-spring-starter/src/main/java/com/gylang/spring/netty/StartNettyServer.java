package com.gylang.spring.netty;

import com.gylang.netty.sdk.DefaultImApplicationContext;
import com.gylang.netty.sdk.ImFactoryBuilder;
import com.gylang.netty.sdk.MessageProvider;
import com.gylang.netty.sdk.call.NotifyContext;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.handler.*;
import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.initializer.CustomInitializer;
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
    private NotifyProvider notifyProvider;
    @Autowired
    private NotifyContext notifyContext;
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

        defaultRequestHandlerAdapter.register(imRequestHandlerList);
        defaultNettyControllerAdapter.register(nettyControllerList);
        defaultNettyControllerAdapter.setDataConverter(dataConverter);

        if (dispatchAdapterHandler instanceof DefaultAdapterDispatch) {
            DefaultAdapterDispatch defaultAdapterDispatch = (DefaultAdapterDispatch) dispatchAdapterHandler;
            defaultAdapterDispatch.setNettyUserInfoFillHandler(nettyUserInfoFillHandler);
            defaultAdapterDispatch.setRequestAdapterList(bizRequestAdapterList);
        }
        DefaultImApplicationContext defaultImApplicationContext = new DefaultImApplicationContext();
        ImFactoryBuilder factoryBuilder = ImFactoryBuilder.builder()
                .notifyProvider(notifyProvider)
                .nettyConfig(nettyConfig)
                .notifyContext(notifyContext)
                .dispatchAdapterHandler(dispatchAdapterHandler)
                .messageProvider(messageProvider)
                .sessionRepository(imSessionRepository)
                .groupRepository(imGroupSessionRepository)
                .dataConverter(dataConverter)
                .build();
        defaultImApplicationContext.doInit(factoryBuilder);
        defaultImApplicationContext.start();
        log.info("初始化基础配置完成 : StartNettyServer");
    }
}
