package com.gylang.spring.netty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.config.NettyProperties;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.event.DefaultEventProvider;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.DefaultMessageRouter;
import com.gylang.netty.sdk.handler.IMessageRouter;
import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.handler.qos.*;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import com.gylang.netty.sdk.initializer.WebSocketJsonInitializer;
import com.gylang.netty.sdk.provider.DefaultMessageProvider;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.spring.netty.custom.adapter.MethodHandlerAdapter;
import com.gylang.spring.netty.custom.method.ControllerMethodMeta;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author gylang
 * data 2020/11/30
 */
@Configuration
@AutoConfigureBefore(ServerStartConfiguration.class)
@Import({ServerStartConfiguration.class, SpringNettyProperties.class})
@ComponentScan("com.gylang.spring.netty.custom")
@Slf4j
public class NettyAutoConfiguration implements InitializingBean {

    @Value("${gylang.netty.converterType:com.gylang.netty.sdk.conveter.JsonConverter}")
    private String converterType;

    @Bean
    @ConditionalOnMissingBean(MessageProvider.class)
    public MessageProvider messageProvider() {

        return new DefaultMessageProvider();
    }


    @Bean
    @ConditionalOnMissingBean(EventProvider.class)
    public EventProvider eventProvider() {
        return new DefaultEventProvider();
    }

    @Bean
    @ConditionalOnMissingBean(IMessageRouter.class)
    public IMessageRouter dispatchAdapterHandler() {

        return new DefaultMessageRouter();
    }

    @Bean
    @ConditionalOnMissingBean(MethodHandlerAdapter.class)
    public BizRequestAdapter<ControllerMethodMeta> bizRequestAdapter() {
        return new MethodHandlerAdapter();
    }

    @Bean
    public EventContext eventContext() {
        return new EventContext();
    }

    @Bean
    public NettyConfiguration nettyConfiguration() {

        return new NettyConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(IMSessionRepository.class)
    public IMSessionRepository imSessionRepository() {
        return new DefaultIMRepository();
    }

    @Bean
    @ConditionalOnMissingBean(IMGroupSessionRepository.class)
    public IMGroupSessionRepository imGroupSessionRepository() {

        return new DefaultGroupRepository();
    }

    @Bean
    @ConditionalOnMissingBean(DataConverter.class)
    public DataConverter dataConverter() {

        return ReflectUtil.newInstance(this.converterType);
    }

    @Bean
    public DefaultNettyControllerAdapter nettyControllerAdapter() {

        return new DefaultNettyControllerAdapter();
    }

    @Bean
    public DefaultRequestHandlerAdapter nettyHandlerAdapter() {

        return new DefaultRequestHandlerAdapter();
    }

    @Bean
    public QosAdapterHandler qosAdapterHandler() {

        return new QosAdapterHandler();
    }

    @Bean
    @ConfigurationProperties("gylang.netty")
    public NettyProperties nettyProperties() {
        return new NettyProperties();
    }


    @Bean
    public List<CustomInitializer<?>> customInitializer(@Autowired NettyProperties properties, @Autowired NettyConfiguration configuration) {
        List<CustomInitializer<?>> customInitializerList = new ArrayList<>();
        Map<String, Integer> socketType = properties.getSocketType();
        if (CollUtil.isEmpty(socketType)) {
            WebSocketJsonInitializer initializer = new WebSocketJsonInitializer();
            initializer.setPort(configuration.getProperties(NettyConfigEnum.WEBSOCKET_PORT));
            customInitializerList.add(initializer);
        } else {
            for (Map.Entry<String, Integer> entry : socketType.entrySet()) {
                Object o = ReflectUtil.newInstance(entry.getKey());
                if (o instanceof CustomInitializer) {
                    CustomInitializer<?> initializer = (CustomInitializer<?>) o;
                    initializer.setPort(entry.getValue());
                    customInitializerList.add(initializer);
                }
            }
        }

        return customInitializerList;
    }

    @Bean
    @ConditionalOnMissingBean(IMessageReceiveQosHandler.class)
    public IMessageReceiveQosHandler iMessageReceiveQosHandler() {
        return new DefaultIMessageReceiveQosHandler();
    }

    @Bean
    @ConditionalOnMissingBean(IMessageSenderQosHandler.class)
    public IMessageSenderQosHandler iMessageSenderQosHandler() {
        return new DefaultIMessageSendQosHandler();
    }

    @Bean
    public ThreadPoolExecutor imExecutor() {
        return new ThreadPoolExecutor(5, 10, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new DefaultThreadFactory("im线程数池"));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("im服务基础数据准备好");
    }
}
