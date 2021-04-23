package com.gylang.spring.netty.custom.adapter;

import cn.hutool.core.collection.CollUtil;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.annotation.NettyMapping;
import com.gylang.netty.sdk.common.InokeFinished;
import com.gylang.netty.sdk.common.MethodWrap;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.util.ObjectWrapUtil;
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
public class MethodHandlerAdapter implements BizRequestAdapter<ControllerMethodMeta> {

    @Autowired
    private MethodArgumentResolverAdapter methodArgumentResolverAdapter;

    private Map<Integer, ControllerMethodMeta> methodHandlerMap;

    @Override
    public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {
        ControllerMethodMeta controllerMethodMeta = methodHandlerMap.get(message.getCmd());
        if (null == controllerMethodMeta) {
            return null;
        }
        MethodArgumentValue methodArgumentValue = new MethodArgumentValue();
        methodArgumentValue.init(controllerMethodMeta);
        Object result = methodArgumentResolverAdapter.handler(ctx, me, message, methodArgumentValue);
        return InokeFinished.finish(result);
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
    public void init(NettyConfiguration configuration) {
        register(configuration.getObjectWrapList());
    }
}
