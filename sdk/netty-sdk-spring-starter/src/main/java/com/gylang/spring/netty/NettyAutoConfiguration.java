package com.gylang.spring.netty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.config.GimProperties;
import com.gylang.netty.sdk.constant.GimDefaultConfigEnum;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.event.DefaultEventProvider;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.DefaultMessageRouter;
import com.gylang.netty.sdk.handler.IMessageRouter;
import com.gylang.netty.sdk.handler.adapter.DefaultGimControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.handler.qos.*;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import com.gylang.netty.sdk.initializer.WebSocketJsonInitializer;
import com.gylang.netty.sdk.provider.DefaultMessageProvider;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import com.gylang.netty.sdk.repo.GIMGroupSessionRepository;
import com.gylang.netty.sdk.repo.GIMSessionRepository;
import com.gylang.spring.netty.custom.adapter.MethodHandlerAdapter;
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
 * 自动配置类
 *
 * @author gylang
 * data 2020/11/30
 */
@Configuration
@AutoConfigureBefore(ServerStartConfiguration.class)
@Import({ServerStartConfiguration.class})
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
    public BizRequestAdapter bizRequestAdapter() {
        return new MethodHandlerAdapter();
    }

    @Bean
    public EventContext eventContext() {
        return new EventContext();
    }

    @Bean
    public GimGlobalConfiguration nettyConfiguration() {

        return new GimGlobalConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(GIMSessionRepository.class)
    public GIMSessionRepository imSessionRepository() {
        return new DefaultIMRepository();
    }

    @Bean
    @ConditionalOnMissingBean(GIMGroupSessionRepository.class)
    public GIMGroupSessionRepository imGroupSessionRepository() {

        return new DefaultGroupRepository();
    }

    @Bean
    @ConditionalOnMissingBean(DataConverter.class)
    public DataConverter dataConverter() {

        return ReflectUtil.newInstance(this.converterType);
    }

    @Bean
    public DefaultGimControllerAdapter nettyControllerAdapter() {

        return new DefaultGimControllerAdapter();
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
    @ConfigurationProperties("gylang.gim")
    public GimProperties nettyProperties() {
        return new GimProperties();
    }


    @Bean
    public List<CustomInitializer<?>> customInitializer(@Autowired GimProperties properties, @Autowired GimGlobalConfiguration configuration) {
        List<CustomInitializer<?>> customInitializerList = new ArrayList<>();
        Map<String, Integer> socketType = properties.getSocketType();
        if (CollUtil.isEmpty(socketType)) {
            WebSocketJsonInitializer initializer = new WebSocketJsonInitializer();
            initializer.setPort(configuration.getProperties(GimDefaultConfigEnum.WEBSOCKET_PORT));
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
        return new DefaultGIMessageReceiveQosHandler();
    }

    @Bean
    @ConditionalOnMissingBean(IMessageSenderQosHandler.class)
    public IMessageSenderQosHandler iMessageSenderQosHandler() {
        return new DefaultGIMessageSendQosHandler();
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
