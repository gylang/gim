package com.gylang.chat;

import com.gylang.netty.sdk.ImApplicationContext;
import com.gylang.netty.sdk.ImFactoryBuilder;
import com.gylang.netty.sdk.DefaultImApplicationContext;
import com.gylang.netty.sdk.call.NotifyContext;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.call.DefaultMessageContext;
import com.gylang.netty.sdk.call.DefaultNotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.conveter.ProtobufConverter;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.handler.*;
import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.initializer.ProtobufInitializer;
import com.gylang.netty.sdk.repo.GroupRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
@Slf4j
public class TestStartConfig {

    private GroupRepository groupRepository;
    private IMSessionRepository sessionRepository = new IMSessionRepository();

    public TestStartConfig() {
        groupRepository = new GroupRepository();
        groupRepository.add("default", new AbstractSessionGroup("默认群聊", 1111L, 50));
    }

    /**
     * netty 服务上下文
     *
     * @return 服务上下文
     */
    public ImApplicationContext imApplicationContext() {

        ImFactoryBuilder build = ImFactoryBuilder.builder()
                .groupRepository(groupRepository)
                .sessionRepository(sessionRepository)
                .messageContext(messageContext())
                .messagePusher(messagePusher())
                .nettyConfig(nettyConfig())
                .requestAdapter(requestAdapter())
                .build();

        DefaultImApplicationContext simpleImApplicationContext = new DefaultImApplicationContext();
        simpleImApplicationContext.doInit(build);
        log.info("启动完成");
        return simpleImApplicationContext;
    }

    /**
     * 请求处理器
     *
     * @return 请求处理器
     */
    public IMRequestAdapter requestAdapter() {
        // 请求适配器 责任链入口
        DefaultAdapterDispatch simpleDispatchHandlerAdapter = new DefaultAdapterDispatch();
        List<IMRequestAdapter> adapterList = new ArrayList<>();
        // 适配器责任链
        // 处理实现IMRequestHandler的业务处理适配器
        DefaultRequestHandlerAdapter simpleRequestHandlerAdapter = new DefaultRequestHandlerAdapter();
        simpleRequestHandlerAdapter.register(requestHandlerList());
        adapterList.add(simpleRequestHandlerAdapter);
        // 处理实现NettyController的业务处理适配器
        DefaultNettyControllerAdapter nettyControllerAdapter = new DefaultNettyControllerAdapter();
        nettyControllerAdapter.setDataConverter(new ProtobufConverter());
        nettyControllerAdapter.register(null);
        adapterList.add(nettyControllerAdapter);
        simpleDispatchHandlerAdapter.setRequestAdapterList(adapterList);
        return simpleDispatchHandlerAdapter;
    }

    /**
     * netty配置
     *
     */
    public NettyConfig nettyConfig() {
        NettyConfig nettyConfig = new NettyConfig();
        nettyConfig.setProperties(new Properties());
        // 初始化器
        ProtobufInitializer jsonInitializer = new ProtobufInitializer(nettyConfig.getProperties(), messagePusher(), requestAdapter());
        nettyConfig.setServerChannelInitializer(jsonInitializer);
        return nettyConfig;
    }

    /**
     * 消息推送器
     *
     * @return 消息推送器
     */
    public NotifyProvider messagePusher() {

        DefaultNotifyProvider simpleMessagePusher = new DefaultNotifyProvider();
        simpleMessagePusher.setMessageContext(messageContext());
        return simpleMessagePusher;
    }

    /**
     * 消息上下文
     */
    public NotifyContext messageContext() {

        DefaultMessageContext simpleMessageContext = new DefaultMessageContext();
        simpleMessageContext.init(null);
        return simpleMessageContext;
    }


    public List<IMRequestHandler> requestHandlerList() {

        JoinGroupHandler joinGroupHandler = new JoinGroupHandler();
        joinGroupHandler.setGroupRepository(groupRepository);
        SimpleChatGroupHandler chatGroupHandler = new SimpleChatGroupHandler();
        chatGroupHandler.setGroupRepository(groupRepository);
        List<IMRequestHandler> requestHandlerList = new ArrayList<>();
        requestHandlerList.add(joinGroupHandler);
        requestHandlerList.add(chatGroupHandler);
        return requestHandlerList;
    }
}
