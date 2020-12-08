package com.gylang.spring.netty.custom.adapter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.gylang.netty.sdk.conveter.MessageConverterAdapter;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.spring.netty.annotation.NettyBody;
import com.gylang.spring.netty.annotation.NettyParam;
import com.gylang.spring.netty.custom.method.MethodMeta;
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
    public boolean support(MethodMeta methodMeta) {

        MethodArgument argumentByAnnotation = MethodArgumentUtils.getArgumentByAnnotation(methodMeta, NettyBody.class);
        return null == argumentByAnnotation;
    }

    @Override
    public boolean handler(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue) {

        // 获取key index
        MethodMeta methodMeta = methodArgumentValue.getMethodMeta();
        Map<String, MethodArgument> argument = methodMeta.getArgument();
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
                    };
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
