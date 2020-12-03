package com.gylang.spring.netty.custom.adapter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.spring.netty.custom.handler.MethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgument;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gylang
 * data 2020/12/2
 */
@Component
@Order(1)
public class InnerArgumentResolveHandler implements MethodArgumentResolverHandler {

    private static final String prefix = "InnerArgumentResolveHandler:";
    private static volatile boolean isInit = false;

    @Override
    public boolean support(MethodMeta methodMeta) {
        Map<String, MethodArgument> argument = methodMeta.getArgument();
        if (CollUtil.isEmpty(argument)) {
            return false;
        }
        if (isInit) {
            return true;
        }
        boolean support = false;
        for (Map.Entry<String, MethodArgument> argumentEntry : argument.entrySet()) {
            MethodArgument methodArgument = argumentEntry.getValue();
            if (ClassUtil.isAssignable(methodArgument.getArgumentType(), ChannelHandlerContext.class)) {
                methodMeta.pushCache(prefix + ChannelHandlerContext.class.getName(), methodArgument.getName());
                support = true;
            } else if (ClassUtil.isAssignable(methodArgument.getArgumentType(), IMSession.class)) {
                methodMeta.pushCache(prefix + IMSession.class.getName(), methodArgument.getName());
                support = true;
            } else if (ClassUtil.isAssignable(methodArgument.getArgumentType(), MessageWrap.class)) {
                methodMeta.pushCache(prefix + MessageWrap.class.getName(), methodArgument.getName());
                support = true;
            }
        }
        if (support) {
            isInit = true;
        }
        return support;
    }

    @Override
    public boolean handler(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue) {

        MethodMeta methodMeta = methodArgumentValue.getMethodMeta();

        putValueIsExist(methodArgumentValue, getMethodArgumentIndex(methodMeta, ChannelHandlerContext.class), ctx);
        putValueIsExist(methodArgumentValue, getMethodArgumentIndex(methodMeta, IMSession.class), me);
        putValueIsExist(methodArgumentValue, getMethodArgumentIndex(methodMeta, MessageWrap.class), message);

        return false;
    }

    private void putValueIsExist(MethodArgumentValue methodArgumentValue, MethodArgument argument, Object val) {
        if (null == argument) {
            return;
        }
        methodArgumentValue.pushIfNullParameter(argument.getArgumentIndex(), val);
    }

    private MethodArgument getMethodArgumentIndex(MethodMeta methodMeta, Class<?> clazz) {
        String cacheIndex = methodMeta.getCache(prefix + clazz.getName());
        return methodMeta.getArgument(cacheIndex);
    }

    public static void main(String[] args) {
        System.out.println(ClassUtil.isAssignable(MethodArgumentResolverHandler.class, InnerArgumentResolveHandler.class));
        System.out.println(ClassUtil.isAssignable(InnerArgumentResolveHandler.class, MethodArgumentResolverHandler.class));
    }
}
