package com.gylang.spring.netty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Singleton;
import com.gylang.netty.sdk.*;
import com.gylang.netty.sdk.annotation.AdapterType;
import com.gylang.netty.sdk.annotation.IMGroupRepository;
import com.gylang.netty.sdk.annotation.IMSessionRepository;
import com.gylang.netty.sdk.call.DefaultMessageContext;
import com.gylang.netty.sdk.call.DefaultNotifyProvider;
import com.gylang.netty.sdk.call.NotifyContext;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.call.message.MessageNotify;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.conveter.JsonConverter;
import com.gylang.netty.sdk.conveter.ProtobufConverter;
import com.gylang.netty.sdk.handler.DefaultAdapterDispatch;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.handler.NettyController;
import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.initializer.ProtobufInitializer;
import com.gylang.netty.sdk.initializer.WebJsonInitializer;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import com.gylang.netty.sdk.repo.FillUserInfoContext;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2020/11/11
 * @version v0.0.1
 */
@Configuration
@Import(IMContextConfig.class)
public class IMAutoConfigration implements InitializingBean {

    /**
     * 消息通知
     */
    @Autowired(required = false)
    private List<MessageNotify<?>> messageNotifyList;
    /**
     * 业务处理类
     */
    @Autowired(required = false)
    private List<IMRequestHandler> requestHandlerList;
    /**
     * 业务处理诶
     */
    @Autowired(required = false)
    private List<NettyController<?>> nettyControllerList;
    /**
     * 消息发送器
     */
    @Autowired(required = false)
    private MessageProvider messageProvider;
    /**
     * netty配置
     */
    @Autowired(required = false)
    private NettyConfig nettyConfig;
    /**
     * 填充数据接口 用户自动填充会话用户信息
     */
    @Autowired(required = false)
    private FillUserInfoContext fillUserInfoContext;
    /**
     * 数据类型转换器
     */
    @Autowired(required = false)
    private DataConverter dataConverter;
    /**
     * 消息发送器
     */
    @Autowired(required = false)
    private NotifyProvider notifyProvider;
    /**
     * 消息通知上下文
     */
    @Autowired(required = false)
    private NotifyContext notifyContext;
    /**
     * im 应用上下文呢
     */
    @Autowired(required = false)
    private ImApplicationContext imApplicationContext;

    @Value("${netty.handler.initializer:json}")
    private String initializer;

    private Object sessionRepository;

    private Object groupRepository;


    /**
     * spring 容器
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IMContext imContext;


    private void initMessageProvider(ThreadPoolExecutor executor, DataConverter dataConverter) {
        DefaultMessageProvider messageProvider = new DefaultMessageProvider(new DefaultIMRepository(), new DefaultGroupRepository(), executor, dataConverter);
        this.messageProvider = null == this.messageProvider ? messageProvider : this.messageProvider;
    }

    private void initNotify(ThreadPoolExecutor executor) {
        notifyContext = null == notifyContext ? createDefaultMessageContext() : notifyContext;
        this.notifyProvider = null == this.notifyProvider ? createDefaultNotifyProvider(executor) : this.notifyProvider;
    }

    private ImApplicationContext initApplication(IMRequestAdapter dispatch) {
        this.imApplicationContext = null == this.imApplicationContext ?
                createDefaultImApplication(dispatch, notifyContext, notifyProvider) : imApplicationContext;
        return imApplicationContext;
    }

    private void initNettyConfig(IMRequestAdapter dispatch) {
        nettyConfig = null == nettyConfig ? createDefaultConfig(dispatch, notifyProvider) : nettyConfig;
    }

    private IMRequestAdapter initAdapter() {
        Map<String, Object> adapter = applicationContext.getBeansWithAnnotation(AdapterType.class);
        IMRequestAdapter dispatch = null;
        List<IMRequestAdapter> bizRequestAdapter = new ArrayList<>();
        List<Object> collect = adapter.values().stream()
                .filter(o -> o instanceof IMRequestAdapter)
                .filter(o -> o.getClass().getAnnotation(AdapterType.class).isDispatch())
                .collect(Collectors.toList());
        Assert.isTrue(1 >= collect.size(), "double dispatchAdapter");
        if (0 == collect.size()) {
            bizRequestAdapter = resolveAdapter(adapter, bizRequestAdapter);
            dispatch = createDefaultDispatch(bizRequestAdapter);
        } else {
            dispatch = (IMRequestAdapter) collect.get(0);
        }
        return dispatch;
    }

    private void createRepository() {
        Map<String, Object> sessionRepository = applicationContext.getBeansWithAnnotation(IMSessionRepository.class);
        if (CollUtil.isEmpty(sessionRepository)) {
            this.sessionRepository = new DefaultIMRepository();
        } else {
            if (sessionRepository.size() > 1) {
                throw new ExceptionInInitializerError("sessionRepository non-uniqueness");
            }
            this.sessionRepository = sessionRepository.values().iterator().next();
        }

        Map<String, Object> groupRepository = applicationContext.getBeansWithAnnotation(IMGroupRepository.class);
        if (CollUtil.isEmpty(groupRepository)) {
            this.groupRepository = new DefaultGroupRepository();
        } else {
            if (groupRepository.size() > 1) {
                throw new ExceptionInInitializerError("sessionRepository non-uniqueness");
            }
            this.groupRepository = groupRepository.values().iterator().next();
        }
    }

    private NettyConfig createDefaultConfig(IMRequestAdapter dispatch, NotifyProvider notifyProvider) {

        if (null == this.nettyConfig) {
            this.nettyConfig = new NettyConfig();
        }
        if (null == this.nettyConfig.getServerChannelInitializer()) {

            if ("json".equals(initializer)) {
                WebJsonInitializer jsonInitializer = new WebJsonInitializer(nettyConfig.getProperties(), notifyProvider, dispatch);
                nettyConfig.setServerChannelInitializer(jsonInitializer);
            } else if ("protobuf".equals(initializer)) {
                ProtobufInitializer protobufInitializer = new ProtobufInitializer(nettyConfig.getProperties(), notifyProvider, dispatch);
                nettyConfig.setServerChannelInitializer(protobufInitializer);
            }
        }
        return this.nettyConfig;
    }

    private DefaultNotifyProvider createDefaultNotifyProvider(ThreadPoolExecutor executor) {
        DefaultNotifyProvider notifyProvider = new DefaultNotifyProvider();
        notifyProvider.setExecutor(executor);
        notifyProvider.setMessageContext(notifyContext);
        return notifyProvider;
    }

    private DefaultMessageContext createDefaultMessageContext() {
        DefaultMessageContext messageContext = new DefaultMessageContext();
        messageContext.init(messageNotifyList);
        return messageContext;
    }

    private DefaultImApplicationContext createDefaultImApplication(IMRequestAdapter dispatch, NotifyContext messageContext, NotifyProvider notifyProvider) {
        DefaultImApplicationContext defaultImApplicationContext = new DefaultImApplicationContext();
        ImFactoryBuilder factoryBuilder = ImFactoryBuilder.builder()
                .messageContext(messageContext)
                .nettyConfig(nettyConfig)
                .notifyProvider(notifyProvider)
                .dispatchAdapter(dispatch)
                .dataConverter(dataConverter)
                .messageProvider(messageProvider)
                .sessionRepository(sessionRepository)
                .groupRepository(groupRepository)
                .build();
        defaultImApplicationContext.doInit(factoryBuilder);
        return defaultImApplicationContext;
    }

    private IMRequestAdapter createDefaultDispatch(List<IMRequestAdapter> bizRequestAdapter) {

        DefaultAdapterDispatch dispatch = new DefaultAdapterDispatch();
        dispatch.setFillUserInfoContext(fillUserInfoContext);
        dispatch.setRequestAdapterList(bizRequestAdapter);
        return dispatch;
    }

    private List<IMRequestAdapter> resolveAdapter(Map<String, Object> adapter, List<IMRequestAdapter> bizRequestAdapter) {
        for (Object bean : adapter.values()) {
            if (bean instanceof IMRequestAdapter) {
                AdapterType adapterType = bean.getClass().getAnnotation(AdapterType.class);
                if (adapterType.isDispatch()) {
                    continue;
                }
                bizRequestAdapter.add((IMRequestAdapter) bean);
            }
        }
        // 添加默认
        bizRequestAdapter.add(createDefaultControllerAdapter());
        bizRequestAdapter.add(createDefaultHandlerAdapter());
        //排序
        bizRequestAdapter = bizRequestAdapter.stream()
                .sorted(Comparator.comparingInt(value -> value.getClass().getAnnotation(AdapterType.class).order()))
                .collect(Collectors.toList());
        return bizRequestAdapter;
    }

    private IMRequestAdapter createDefaultHandlerAdapter() {

        DefaultRequestHandlerAdapter adapter = new DefaultRequestHandlerAdapter();
        adapter.register(requestHandlerList);
        return adapter;
    }

    private IMRequestAdapter createDefaultControllerAdapter() {
        DefaultNettyControllerAdapter adapter = new DefaultNettyControllerAdapter();
        adapter.register(nettyControllerList);
        adapter.setDataConverter(createConverter());
        return adapter;
    }

    private DataConverter createConverter() {
        if (null != dataConverter) {
            return dataConverter;
        }
        if ("json".equals(initializer)) {
            return Singleton.get(JsonConverter.class);
        } else if ("protobuf".equals(initializer)) {
            return Singleton.get(ProtobufConverter.class);
        }
        //默认
        return Singleton.get(JsonConverter.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 赋值默认
        ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 92, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new DefaultThreadFactory("im线程数池"));

        dataConverter = createConverter();

        initMessageProvider(executor, dataConverter);
        // 初始化消息推送
        initNotify(executor);

        // 获取处理适配器
        IMRequestAdapter dispatch = initAdapter();

        initNettyConfig(dispatch);

        createRepository();

        // netty 服务启动器
        ImApplicationContext imApplicationContext = initApplication(dispatch);
        imApplicationContext.start();
        // 构建bean

        BeanUtils.copyProperties(imApplicationContext.imContext(), this.imContext);
        System.out.println(imContext);
    }
}
