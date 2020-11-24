package com.gylang.spring.netty.register;

import com.gylang.netty.sdk.call.DefaultNotifyContext;
import com.gylang.netty.sdk.call.DefaultNotifyProvider;
import com.gylang.netty.sdk.call.NotifyContext;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.call.message.MessageNotify;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2020/11/20
 */
@Component
public class IMNotifyRegister implements BeanDefinitionRegistryPostProcessor {

    @Resource(name = "imExecutor")
    private ThreadPoolExecutor executor;
    @Autowired(required = false)
    private List<MessageNotify<?>> messageNotifyList;
    @Autowired(required = false)
    private NotifyContext notifyContext;
    @Autowired(required = false)
    private NotifyProvider notifyProvider;
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        // 注册 消息上下文
        if (null == notifyContext) {
            notifyContext = new DefaultNotifyContext();
            configurableListableBeanFactory.registerSingleton("notifyContext", notifyContext);

        }
        if (null != messageNotifyList) {
            messageNotifyList.forEach(notifyContext::register);
        }
        // 注册消息通知发送器
        if (null == notifyProvider) {
            DefaultNotifyProvider defaultNotifyProvider = new DefaultNotifyProvider();
            defaultNotifyProvider.setExecutor(executor);
            configurableListableBeanFactory.registerSingleton("notifyProvider", defaultNotifyProvider);
        }
    }
}
