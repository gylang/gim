package com.gylang.spring.netty.register;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.DefaultMessageProvider;
import com.gylang.netty.sdk.MessageProvider;
import com.gylang.netty.sdk.annotation.IMGroupRepository;
import com.gylang.netty.sdk.annotation.IMRepository;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import com.gylang.spring.netty.constant.NameConst;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2020/11/21
 */
@Component
public class MessageProviderRegister implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        MessageProvider bean = null;
        try {
            bean = beanFactory.getBean(MessageProvider.class);
        } catch (BeansException ignored) {

        }
        if (null == bean) {

            Map<String, Object> sessionRepository = beanFactory.getBeansWithAnnotation(IMRepository.class);
                DefaultIMRepository defaultIMRepository = new DefaultIMRepository();
            if (CollUtil.isEmpty(sessionRepository)) {
                beanFactory.registerSingleton(NameConst.IM_REPOSITORY, defaultIMRepository);
            }
            Map<String, Object> groupRepository = beanFactory.getBeansWithAnnotation(IMGroupRepository.class);
            DefaultGroupRepository defaultGroupRepository = new DefaultGroupRepository();
            if (CollUtil.isEmpty(groupRepository)) {
                beanFactory.registerSingleton(NameConst.IM_GROUP_REPOSITORY, defaultGroupRepository);
            }
            DefaultMessageProvider defaultMessageProvider = new DefaultMessageProvider(defaultIMRepository, defaultGroupRepository, (ThreadPoolExecutor) beanFactory.getBean("imExecutor"), beanFactory.getBean(DataConverter.class));
            beanFactory.registerSingleton(NameConst.MESSAGE_PROVIDER, defaultMessageProvider);
        }
    }
}
