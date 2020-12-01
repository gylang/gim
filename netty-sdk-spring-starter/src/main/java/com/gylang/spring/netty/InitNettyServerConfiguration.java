package com.gylang.spring.netty;

import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.MessageProvider;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.handler.DispatchAdapterHandler;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * @author gylang
 * data 2020/12/1
 */
@Configuration
public class InitNettyServerConfiguration {

    @Resource
    private MessageProvider messageProvider;
    @Value("${im.initializerType:com.gylang.netty.sdk.initializer.WebJsonInitializer}")
    private String initializerType;
    @Resource
    private NotifyProvider notifyProvider;
    @Resource
    private NettyConfig nettyConfig;
    @Resource
    private DispatchAdapterHandler dispatchAdapterHandler;

    @Bean
    @ConditionalOnMissingBean(CustomInitializer.class)
    public CustomInitializer<?> customInitializer() {
        CustomInitializer<?> initializer = null;
        try {
            Constructor constructor = ReflectUtil.getConstructor(Class.forName(initializerType), Properties.class, NotifyProvider.class, IMRequestAdapter.class);
            initializer = (CustomInitializer<?>) constructor.newInstance(nettyConfig.getProperties(), notifyProvider, dispatchAdapterHandler);
            nettyConfig.setServerChannelInitializer(initializer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return initializer;
    }

}
