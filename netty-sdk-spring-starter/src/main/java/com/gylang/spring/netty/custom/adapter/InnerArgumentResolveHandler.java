package com.gylang.spring.netty.custom.adapter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.spring.netty.custom.method.ControllerMethodMeta;
import com.gylang.spring.netty.custom.handler.MethodArgumentResolverHandler;
import com.gylang.spring.netty.custom.method.MethodArgument;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 内置参数解析器
 *
 * @author gylang
 * data 2020/12/2
 */
@Component
@Order(1)
public class InnerArgumentResolveHandler implements MethodArgumentResolverHandler {

    private static final String PREFIX = "InnerArgumentResolveHandler:";
    private volatile boolean hasInit = false;
    private volatile boolean cacheSupport = false;

    @Override
    public boolean support(ControllerMethodMeta controllerMethodMeta) {
        Map<String, MethodArgument> argument = controllerMethodMeta.getArgument();
        if (CollUtil.isEmpty(argument)) {
            return false;
        }
        if (hasInit) {
            return cacheSupport;
        }
        boolean support = false;
        // 判断参数是否包包含内置参数
        for (Map.Entry<String, MethodArgument> argumentEntry : argument.entrySet()) {
            MethodArgument methodArgument = argumentEntry.getValue();
            if (ClassUtil.isAssignable(methodArgument.getArgumentType(), ChannelHandlerContext.class)) {
                controllerMethodMeta.pushCache(PREFIX + ChannelHandlerContext.class.getName(), methodArgument.getName());
                support = true;
            } else if (ClassUtil.isAssignable(methodArgument.getArgumentType(), IMSession.class)) {
                controllerMethodMeta.pushCache(PREFIX + IMSession.class.getName(), methodArgument.getName());
                support = true;
            } else if (ClassUtil.isAssignable(methodArgument.getArgumentType(), MessageWrap.class)) {
                controllerMethodMeta.pushCache(PREFIX + MessageWrap.class.getName(), methodArgument.getName());
                support = true;
            }
        }
        if (support) {
            hasInit = true;
            support = cacheSupport;
        }
        return support;
    }

    @Override
    public boolean handler(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue) {

        ControllerMethodMeta controllerMethodMeta = methodArgumentValue.getControllerMethodMeta();

        putValueIsExist(methodArgumentValue, getMethodArgumentIndex(controllerMethodMeta, ChannelHandlerContext.class), ctx);
        putValueIsExist(methodArgumentValue, getMethodArgumentIndex(controllerMethodMeta, IMSession.class), me);
        putValueIsExist(methodArgumentValue, getMethodArgumentIndex(controllerMethodMeta, MessageWrap.class), message);

        return false;
    }

    private void putValueIsExist(MethodArgumentValue methodArgumentValue, MethodArgument argument, Object val) {
        if (null == argument) {
            return;
        }
        methodArgumentValue.pushIfNullParameter(argument.getArgumentIndex(), val);
    }

    private MethodArgument getMethodArgumentIndex(ControllerMethodMeta controllerMethodMeta, Class<?> clazz) {
        String cacheIndex = controllerMethodMeta.getCache(PREFIX + clazz.getName());
        return controllerMethodMeta.getArgument(cacheIndex);
    }


}
