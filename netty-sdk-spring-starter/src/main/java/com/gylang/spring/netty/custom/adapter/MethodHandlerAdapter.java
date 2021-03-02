package com.gylang.spring.netty.custom.adapter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gylang.netty.sdk.annotation.AdapterType;
import com.gylang.netty.sdk.annotation.NettyMapping;
import com.gylang.netty.sdk.common.MethodWrap;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.util.ObjectWrapUtil;
import com.gylang.spring.netty.annotation.SpringNettyController;
import com.gylang.spring.netty.custom.handler.ControllerMethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import com.gylang.spring.netty.custom.reslove.MethodArgumentResolver;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Component
@AdapterType
@Slf4j
public class MethodHandlerAdapter implements BizRequestAdapter<ControllerMethodMeta> {

    @Autowired
    private MethodArgumentResolverAdapter methodArgumentResolverAdapter;

    @Autowired
    private NettyConfiguration nettyConfiguration;

    private Map<String, ControllerMethodMeta> methodHandlerMap;

    @Override
    public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {
        ControllerMethodMeta controllerMethodMeta = methodHandlerMap.get(message.getCmd());
        if (null == controllerMethodMeta) {
            return null;
        }
        MethodArgumentValue methodArgumentValue = new MethodArgumentValue();
        methodArgumentValue.init(controllerMethodMeta);
        return methodArgumentResolverAdapter.handler(ctx, me, message, methodArgumentValue);
    }


    @Override
    public void register(List<ObjectWrap> processList) {

        this.methodHandlerMap = CollUtil.newHashMap();
        if (!CollUtil.isEmpty(processList)) {
            for (ObjectWrap objectWrap : processList) {
                SpringNettyController nettyController = ObjectWrapUtil.findAnnotation(SpringNettyController.class, objectWrap);

                List<MethodWrap> methodWrapList = objectWrap.getMethodWrapList();
                if (CollUtil.isNotEmpty(methodWrapList)) {
                    for (MethodWrap methodWrap : methodWrapList) {
                        NettyMapping nettyMapping = ObjectWrapUtil.findAnnotation(NettyMapping.class, methodWrap);
                        if (null != nettyMapping) {
                            ControllerMethodMeta controllerMethodMeta = new ControllerMethodMeta();
                            controllerMethodMeta.setMethod(methodWrap.getMethod());
                            controllerMethodMeta.setNettyController(nettyController);
                            controllerMethodMeta.setNettyMapping(nettyMapping);
                            controllerMethodMeta.setClazz(objectWrap.getClass());
                            controllerMethodMeta.setInstance(objectWrap.getInstance());
                            controllerMethodMeta.setAnnotationList(new HashSet<>(objectWrap.getAnnotationList()));
                            methodHandlerMap.put(nettyMapping.value(), controllerMethodMeta);
                        }
                    }
                }
            }


        }
    }

    private String getBizKey(ControllerMethodMeta controllerMethodMeta) {

        SpringNettyController nettyController = controllerMethodMeta.getNettyController();

        NettyMapping nettyMapping = controllerMethodMeta.getNettyMapping();
        return StrUtil.isBlank(nettyController.value()) ? nettyMapping.value() : nettyController.value() + ":" + nettyMapping.value();
    }




    @Override
    public void init(NettyConfiguration configuration) {
        register(configuration.getObjectWrapList());
    }
}
