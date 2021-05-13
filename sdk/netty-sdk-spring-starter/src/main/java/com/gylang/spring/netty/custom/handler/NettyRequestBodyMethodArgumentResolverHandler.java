package com.gylang.spring.netty.custom.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.spring.netty.annotation.NettyBody;
import com.gylang.spring.netty.custom.method.ControllerMethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgument;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import com.gylang.spring.netty.util.MethodArgumentUtils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 参数体解析器
 *
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Component
public class NettyRequestBodyMethodArgumentResolverHandler implements MethodArgumentResolverHandler {

    String cacheName = "NettyRequestBody";


    @Override
    public boolean support(ControllerMethodMeta controllerMethodMeta) {
        String cache = controllerMethodMeta.getCache(cacheName);
        if (null != cache) {
            return true;
        }
        MethodArgument methodArgument = MethodArgumentUtils.getArgumentByAnnotation(controllerMethodMeta, NettyBody.class);
        if (null != methodArgument) {
            controllerMethodMeta.pushCache(cacheName, methodArgument.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean handler(ChannelHandlerContext ctx, GIMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue) {

        ControllerMethodMeta controllerMethodMeta = methodArgumentValue.getControllerMethodMeta();
        String cache = controllerMethodMeta.getCache(cacheName);
        MethodArgument argument = controllerMethodMeta.getArgument(cache);
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
