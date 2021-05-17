package com.gylang.spring.netty.custom.adapter;

import cn.hutool.core.collection.CollUtil;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.annotation.NettyMapping;
import com.gylang.netty.sdk.api.common.InvokeFinished;
import com.gylang.netty.sdk.api.common.MethodWrap;
import com.gylang.netty.sdk.api.common.ObjectWrap;
import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.BizRequestAdapter;
import com.gylang.netty.sdk.api.util.ObjectWrapUtil;
import com.gylang.spring.netty.annotation.SpringNettyController;
import com.gylang.spring.netty.custom.method.ControllerMethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 模仿类似spring的控制层, 实现参数解析调用,性能方面可能存在影响. 使用方便
 *
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Component
@Slf4j
public class MethodHandlerAdapter implements BizRequestAdapter {

    @Autowired
    private MethodArgumentResolverAdapter methodArgumentResolverAdapter;

    private Map<Integer, ControllerMethodMeta> methodHandlerMap;

    @Override
    public Object process(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {
        ControllerMethodMeta controllerMethodMeta = methodHandlerMap.get(message.getType());
        if (null == controllerMethodMeta) {
            return null;
        }
        MethodArgumentValue methodArgumentValue = new MethodArgumentValue();
        methodArgumentValue.init(controllerMethodMeta);
        Object result = methodArgumentResolverAdapter.handler(ctx, me, message, methodArgumentValue);
        return InvokeFinished.finish(result);
    }


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
                            Integer bizKey = nettyMapping.value();
                            ControllerMethodMeta controllerMethodMeta = new ControllerMethodMeta();
                            controllerMethodMeta.setMethod(methodWrap.getMethod());
                            controllerMethodMeta.setNettyController(nettyController);
                            controllerMethodMeta.setNettyMapping(nettyMapping);
                            controllerMethodMeta.setClazz(objectWrap.getClass());
                            controllerMethodMeta.setInstance(objectWrap.getInstance());
                            controllerMethodMeta.setAnnotationList(new HashSet<>(objectWrap.getAnnotationList()));
                            methodHandlerMap.put(bizKey, controllerMethodMeta);
                        }
                    }
                }
            }


        }
    }

    @Override
    public Integer order() {
        return null;
    }




    @Override
    public void init(GimGlobalConfiguration configuration) {
        register(configuration.getObjectWrapList());
    }
}
