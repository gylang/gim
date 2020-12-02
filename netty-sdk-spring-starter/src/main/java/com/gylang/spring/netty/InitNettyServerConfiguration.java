package com.gylang.spring.netty;

import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.MessageProvider;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.handler.DispatchAdapterHandler;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 * @author gylang
 * data 2020/12/1
 */
@Configuration
@Import(StartNettyServer.class)
@AutoConfigureBefore(StartNettyServer.class)
@Slf4j
public class InitNettyServerConfiguration implements InitializingBean {

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


    @Override
    public void afterPropertiesSet() throws Exception {

        log.info("初始化基础配置完成 : InitNettyServerConfiguration");

    }
}
