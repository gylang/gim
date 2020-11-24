package com.gylang.spring.netty.register;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import io.netty.channel.ChannelInitializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * @author gylang
 * data 2020/11/21
 */
@Component
public class ChannelInitializerRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private String initializerType;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        ChannelInitializer bean = null;
        try {
            bean = beanFactory.getBean(ChannelInitializer.class);
        } catch (BeansException ignored) {

        }
        if (null == bean) {
            Constructor constructor = null;
            try {
                constructor = ReflectUtil.getConstructor(Class.forName(initializerType), Properties.class, NotifyProvider.class, IMRequestAdapter.class);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            NettyConfig nettyConfig = null;
            try {
                nettyConfig = beanFactory.getBean(NettyConfig.class);
            } catch (BeansException ignored) {
            }
            if (null == nettyConfig) {
                nettyConfig = new NettyConfig();
                beanFactory.registerSingleton("nettyConfig", nettyConfig);
            }
            Object initializer = null;
            try {
                initializer = constructor.newInstance(nettyConfig.getProperties(), beanFactory.getBean(NotifyProvider.class), beanFactory.getBean("imAdapterDispatch"));
                nettyConfig.setServerChannelInitializer((ChannelInitializer<?>) initializer);
                beanFactory.registerSingleton("nettyInitializer", initializer);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void setEnvironment(Environment environment) {
        String property = environment.getProperty("im.initializerType");
        initializerType = null == property ? "com.gylang.netty.sdk.initializer.WebJsonInitializer" : property;
    }
}
