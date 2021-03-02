//package com.gylang.chat;
//
//import com.gylang.netty.sdk.call.DefaultEventContext;
//import com.gylang.netty.sdk.call.DefaultEventProvider;
//import com.gylang.netty.sdk.call.EventContext;
//import com.gylang.netty.sdk.call.EventProvider;
//import com.gylang.netty.sdk.config.NettyConfig;
//import com.gylang.netty.sdk.conveter.ProtobufConverter;
//import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
//import com.gylang.netty.sdk.handler.BizRequestAdapter;
//import com.gylang.netty.sdk.handler.DefaultAdapterDispatch;
//import com.gylang.netty.sdk.handler.IRequestAdapter;
//import com.gylang.netty.sdk.handler.IMRequestHandler;
//import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
//import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
//import com.gylang.netty.sdk.initializer.ProtobufInitializer;
//import com.gylang.netty.sdk.repo.DefaultGroupRepository;
//import com.gylang.netty.sdk.repo.DefaultIMRepository;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @author gylang
// * data 2020/11/9
// * @version v0.0.1
// */
//@Slf4j
//public class TestStartConfig {
//
//    private DefaultGroupRepository defaultGroupRepository;
//    private DefaultIMRepository sessionRepository = new DefaultIMRepository();
//
//    public TestStartConfig() {
//        defaultGroupRepository = new DefaultGroupRepository();
//        defaultGroupRepository.add("default", new AbstractSessionGroup("默认群聊", "1111", 50));
//    }
//
//
//    /**
//     * 请求处理器
//     *
//     * @return 请求处理器
//     */
//    public IRequestAdapter<?> requestAdapter() {
//        // 请求适配器 责任链入口
//        DefaultAdapterDispatch simpleDispatchHandlerAdapter = new DefaultAdapterDispatch();
//        List<BizRequestAdapter<?>> adapterList = new ArrayList<>();
//        // 适配器责任链
//        // 处理实现IMRequestHandler的业务处理适配器
//        DefaultRequestHandlerAdapter simpleRequestHandlerAdapter = new DefaultRequestHandlerAdapter();
//        simpleRequestHandlerAdapter.register(requestHandlerList());
//        adapterList.add(simpleRequestHandlerAdapter);
//        // 处理实现NettyController的业务处理适配器
//        DefaultNettyControllerAdapter nettyControllerAdapter = new DefaultNettyControllerAdapter();
//        nettyControllerAdapter.setDataConverter(new ProtobufConverter());
//        nettyControllerAdapter.register(null);
//        adapterList.add(nettyControllerAdapter);
//        simpleDispatchHandlerAdapter.setRequestAdapterList(adapterList);
//        return simpleDispatchHandlerAdapter;
//    }
//
//    /**
//     * netty配置
//     */
//    public NettyConfig nettyConfig() {
//        NettyConfig nettyConfig = new NettyConfig();
//        nettyConfig.setProperties(new HashMap<>());
//        // 初始化器
//        ProtobufInitializer jsonInitializer = new ProtobufInitializer();
//        nettyConfig.setServerChannelInitializer(jsonInitializer);
//        return nettyConfig;
//    }
//
//    /**
//     * 消息推送器
//     *
//     * @return 消息推送器
//     */
//    public EventProvider messagePusher() {
//
//        DefaultEventProvider simpleMessagePusher = new DefaultEventProvider();
//        return simpleMessagePusher;
//    }
//
//    /**
//     * 消息上下文
//     */
//    public EventContext messageContext() {
//
//        DefaultEventContext simpleMessageContext = new DefaultEventContext();
//        simpleMessageContext.init(null);
//        return simpleMessageContext;
//    }
//
//
//    public List<IMRequestHandler> requestHandlerList() {
//
//        JoinGroupHandler joinGroupHandler = new JoinGroupHandler();
//        joinGroupHandler.setDefaultGroupRepository(defaultGroupRepository);
//        SimpleChatGroupHandler chatGroupHandler = new SimpleChatGroupHandler();
//        chatGroupHandler.setDefaultGroupRepository(defaultGroupRepository);
//        List<IMRequestHandler> requestHandlerList = new ArrayList<>();
//        requestHandlerList.add(joinGroupHandler);
//        requestHandlerList.add(chatGroupHandler);
//        return requestHandlerList;
//    }
//}
