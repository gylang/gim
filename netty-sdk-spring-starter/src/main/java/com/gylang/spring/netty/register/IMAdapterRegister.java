package com.gylang.spring.netty.register;

import com.gylang.netty.sdk.annotation.AdapterType;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.handler.DefaultAdapterDispatch;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.handler.NettyController;
import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.repo.FillUserInfoContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2020/11/20
 */
@Configuration
@AutoConfigureBefore
public class IMAdapterRegister implements BeanDefinitionRegistryPostProcessor {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        // 分发器
//        String[] adapterName = configurableListableBeanFactory.getBeanNamesForType(IMRequestAdapter.class);
//        List<IMRequestAdapter> requestAdapterList = Arrays.stream(adapterName)
//                .map(a -> configurableListableBeanFactory.getBean(a, IMRequestAdapter.class))
//                .collect(Collectors.toList());
//
//        List<IMRequestAdapter> dispatchList = requestAdapterList.stream()
//                .filter(this::isDispatch)
//                .collect(Collectors.toList());
//
//        Assert.isTrue(1 >= dispatchList.size(), "double dispatchAdapter");
        List<IMRequestAdapter> requestAdapterList = new ArrayList<>();
        IMRequestAdapter dispatch = null;
//        if (0 == dispatchList.size()) {
        dispatch = createDefaultDispatch(requestAdapterList, configurableListableBeanFactory);
//        } else {
//            dispatch = (IMRequestAdapter) dispatchList.get(0);
//            requestAdapterList.remove(dispatch);
//        }
        requestAdapterList.add(createDefaultControllerAdapter(configurableListableBeanFactory));
        requestAdapterList.add(createDefaultHandlerAdapter(configurableListableBeanFactory));
        dispatch.register(requestAdapterList);
        configurableListableBeanFactory.registerSingleton("imAdapterDispatch", dispatch);
    }

    private IMRequestAdapter createDefaultControllerAdapter(ConfigurableListableBeanFactory beanFactory) {

        DefaultNettyControllerAdapter adapter = new DefaultNettyControllerAdapter();
        adapter.setDataConverter(beanFactory.getBean(DataConverter.class));
        String[] handlerName = beanFactory.getBeanNamesForType(NettyController.class);

        List<NettyController> requestControllerList = Arrays.stream(handlerName)
                .map(s -> beanFactory.getBean(s, NettyController.class))
                .collect(Collectors.toList());
        adapter.register(requestControllerList);
        return adapter;
    }

    private IMRequestAdapter createDefaultHandlerAdapter(ConfigurableListableBeanFactory beanFactory) {

        String[] handlerName = beanFactory.getBeanNamesForType(IMRequestHandler.class);
        List<IMRequestHandler> requestHandlerList = Arrays.stream(handlerName)
                .map(s -> beanFactory.getBean(s, IMRequestHandler.class))
                .collect(Collectors.toList());
        DefaultRequestHandlerAdapter adapter = new DefaultRequestHandlerAdapter();
        adapter.register(requestHandlerList);
        return adapter;
    }

    private IMRequestAdapter createDefaultDispatch(List<IMRequestAdapter> bizRequestAdapter, ConfigurableListableBeanFactory beanFactory) {

        DefaultAdapterDispatch defaultAdapterDispatch = new DefaultAdapterDispatch();
        try {
            defaultAdapterDispatch.setFillUserInfoContext(beanFactory.getBean(FillUserInfoContext.class));
        } catch (BeansException ignored) {
        }
        defaultAdapterDispatch.setRequestAdapterList(bizRequestAdapter);

        // 默认规则的适配器
        return defaultAdapterDispatch;
    }


    private boolean isDispatch(IMRequestAdapter imRequestAdapter) {

        AdapterType annotation = imRequestAdapter.getClass().getAnnotation(AdapterType.class);
        return null != annotation && annotation.isDispatch();
    }
}
