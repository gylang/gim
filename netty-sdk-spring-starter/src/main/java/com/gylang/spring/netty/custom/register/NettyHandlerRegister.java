package com.gylang.spring.netty.custom.register;

import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gylang
 * data 2021/3/2
 */
@Component
@Slf4j
public class NettyHandlerRegister implements InitializingBean {

    @Resource
    private NettyConfiguration nettyConfiguration;

    @Resource
    private List<IMRequestHandler> imRequestHandlerList;

    @Override
    public void afterPropertiesSet() {
        log.info("初始化方法处理器");

        List<ObjectWrap> objectWrapList = new ArrayList<>();
        for (IMRequestHandler handler : imRequestHandlerList) {

            ObjectWrap objectWrap = new ObjectWrap();
            Class<?> userClass = ClassUtils.getUserClass(handler);
            objectWrap.setInstance(handler);
            NettyHandler nettyHandler = AnnotationUtils.findAnnotation(userClass, NettyHandler.class);
            objectWrap.addAnno(nettyHandler);
            objectWrap.setUserType(userClass);
            objectWrapList.add(objectWrap);
        }
        nettyConfiguration.addObjectWrap(objectWrapList);
    }
}