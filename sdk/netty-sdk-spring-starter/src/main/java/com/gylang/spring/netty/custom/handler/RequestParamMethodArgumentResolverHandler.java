package com.gylang.spring.netty.custom.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.spring.netty.annotation.NettyBody;
import com.gylang.spring.netty.annotation.NettyParam;
import com.gylang.spring.netty.custom.adapter.MessageConverterAdapter;
import com.gylang.spring.netty.custom.method.ControllerMethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgument;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import com.gylang.spring.netty.util.MethodArgumentUtils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author gylang
 * data 2020/11/27
 * @version v0.0.1
 */
@Component
public class RequestParamMethodArgumentResolverHandler implements MethodArgumentResolverHandler {

    @Autowired
    private List<MessageConverterAdapter> messageConverterAdapterList;

    @Override
    public boolean support(ControllerMethodMeta controllerMethodMeta) {

        MethodArgument argumentByAnnotation = MethodArgumentUtils.getArgumentByAnnotation(controllerMethodMeta, NettyBody.class);
        return null == argumentByAnnotation;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handler(ChannelHandlerContext ctx, GIMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue) {

        // 获取key index
        ControllerMethodMeta controllerMethodMeta = methodArgumentValue.getControllerMethodMeta();
        Map<String, MethodArgument> argument = controllerMethodMeta.getArgument();
        if (null == argument) {
            return false;
        }
        for (MessageConverterAdapter<Object> messageConverterAdapter : messageConverterAdapterList) {

            boolean support = messageConverterAdapter.support(message, JSONObject.class);
            if (support) {
                JSONObject jsonObject = messageConverterAdapter.resolve(message, JSONObject.class);
                for (Map.Entry<String, MethodArgument> argumentEntry : argument.entrySet()) {
                    MethodArgument methodArgument = argumentEntry.getValue();
                    NettyParam nettyParam = MethodArgumentUtils.getAnnotationFormArgument(methodArgument, NettyParam.class);
                    String key = getKey(nettyParam, argumentEntry.getKey());
                    String argumentValue = jsonObject.getString(key);
                    if (null == argumentValue && isRequire(nettyParam)) {
                        throw new IllegalArgumentException(key + ": 参数为空");
                    }
                    if (null != argumentValue) {
                        methodArgumentValue.pushIfNullParameter(methodArgument.getArgumentIndex(), argumentValue);
                    }

                }
            }
        }
        return false;
    }

    private boolean isRequire(NettyParam nettyParam) {
        return null != nettyParam && nettyParam.require();
    }

    private String getKey(NettyParam nettyParam, String key) {
        if (null == nettyParam) {
            return key;
        }
        return StrUtil.isEmpty(nettyParam.value()) ? key : nettyParam.value();
    }
}
