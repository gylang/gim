package com.gylang.spring.netty.custom.adapter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.spring.netty.annotation.NettyBody;
import com.gylang.spring.netty.custom.handler.MethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgument;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import com.gylang.spring.netty.util.MethodArgumentUtils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Component
public class NettyRequestBodyMethodArgumentResolverHandler implements MethodArgumentResolverHandler {

    String cacheName = "NettyRequestBody";

    @Override
    public boolean support(MethodMeta methodMeta) {
        String cache = methodMeta.getCache(cacheName);
        if (null != cache) {
            return true;
        }
        MethodArgument methodArgument = MethodArgumentUtils.getArgumentByAnnotation(methodMeta, NettyBody.class);
        if (null != methodArgument) {
            methodMeta.pushCache(cacheName, methodArgument.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean handler(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue) {

        MethodMeta methodMeta = methodArgumentValue.getMethodMeta();
        String cache = methodMeta.getCache(cacheName);
        MethodArgument argument = methodMeta.getArgument(cache);
        boolean simpleValueType = ClassUtil.isSimpleValueType(argument.getArgumentType());
        Object body;
        if (simpleValueType) {
            body = Convert.convert(argument.getArgumentType(), message.getContent());
        } else {
            body = JSON.parseObject(message.getContent(), argument.getArgumentType());
        }
        methodArgumentValue.pushIfNullParameter(argument.getArgumentIndex(), body);
        return true;
    }
}
