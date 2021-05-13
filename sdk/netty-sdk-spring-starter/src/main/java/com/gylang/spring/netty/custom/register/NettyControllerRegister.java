package com.gylang.spring.netty.custom.register;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.handler.GimController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NettyControllerRegister implements InitializingBean {

    @Resource
    private GimGlobalConfiguration gimGlobalConfiguration;

    @Autowired(required = false)
    private List<GimController> gimControllerList;
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("加载 controller消息处理器");

        List<ObjectWrap> objectWrapList = new ArrayList<>();
        if (CollUtil.isEmpty(gimControllerList)) {
            return;
        }
        for (GimController handler : gimControllerList) {

            ObjectWrap objectWrap = new ObjectWrap();
            Class<?> userClass = ClassUtils.getUserClass(handler);
            objectWrap.setInstance(handler);
            NettyHandler nettyHandler = AnnotationUtils.findAnnotation(userClass, NettyHandler.class);
            objectWrap.addAnnotation(nettyHandler);
            objectWrap.setUserType(userClass);
            objectWrapList.add(objectWrap);
        }
        gimGlobalConfiguration.addObjectWrap(objectWrapList);
    }
}