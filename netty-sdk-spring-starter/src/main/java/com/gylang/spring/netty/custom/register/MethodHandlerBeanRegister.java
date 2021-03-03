package com.gylang.spring.netty.custom.register;

import com.gylang.netty.sdk.common.MethodWrap;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.spring.netty.annotation.SpringNettyController;
import com.gylang.spring.netty.custom.reslove.MethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gylang
 * data 2021/3/2
 */
@Component
@Slf4j
public class MethodHandlerBeanRegister implements InitializingBean {

    @Resource
    private NettyConfiguration nettyConfiguration;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private MethodArgumentResolver methodArgumentResolver;
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("加载 method 消息处理器");
        Map<String, Object> controllerMap = applicationContext.getBeansWithAnnotation(SpringNettyController.class);
        List<ObjectWrap> objectWrapList = new ArrayList<>();
        for (Object controller : controllerMap.values()) {
            Class<?> clazz = ClassUtils.getUserClass(controller);
            ObjectWrap objectWrap = new ObjectWrap();
            objectWrap.setInstance(controller);
            objectWrap.setUserType(clazz);
            SpringNettyController nettyController = AnnotationUtils.findAnnotation(clazz, SpringNettyController.class);
            objectWrap.addAnno(nettyController);
            List<MethodWrap> methodWrapList = new ArrayList<>();
            for (Method method : clazz.getDeclaredMethods()) {
                if (methodArgumentResolver.support(method)) {
                    MethodWrap methodWrap = methodArgumentResolver.resolve(method);
                    methodWrap.setObject(controller);
                    methodWrapList.add(methodWrap);
                }
            }
            objectWrap.setMethodWrapList(methodWrapList);
            objectWrapList.add(objectWrap);
        }
        nettyConfiguration.addObjectWrap(objectWrapList);
    }
}
