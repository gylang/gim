package com.gylang.spring.netty.register;

import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.conveter.DataConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2020/11/20
 */
@Component
public class DataConverterRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private String converterType;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        DataConverter converter = null;
        try {
            converter = configurableListableBeanFactory.getBean(DataConverter.class);
        } catch (BeansException ignored) {

        }

        if (null == converter) {
            configurableListableBeanFactory.registerSingleton("dataConverter", ReflectUtil.newInstance(this.converterType));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        String property = environment.getProperty("im.converterType");
        this.converterType = null == property ? "com.gylang.netty.sdk.conveter.JsonConverter" : property;
    }
}
