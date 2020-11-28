package com.gylang.spring.netty.custom.reslove;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.annotation.NettyMapping;
import com.gylang.spring.netty.custom.handler.MethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgument;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Component
public class MethodArgumentResolver implements AbstractResolver<Method, MethodMeta> {




    @Override
    public boolean support(Method method) {
        return method.isAnnotationPresent(NettyMapping.class);
    }

    @Override
    public MethodMeta resolve(Method method) {

        // 方法处理器 用户存储方法信息 和 参数信息
        MethodMeta methodMeta = MethodMeta.builder()
                .annotationList(CollUtil.newHashSet(method.getAnnotations()))
                .method(method)
                .nettyMapping(method.getAnnotation(NettyMapping.class))
                .build();
        // 解析参数
         LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        if (null != parameterNames && null != parameters && parameterNames.length == parameters.length) {
            HashMap<String, MethodArgument> argument = CollUtil.newHashMap(parameters.length);
            methodMeta.setArgument(argument);
            for (int i = 0; i < parameterNames.length; i++) {
                MethodArgument methodArgument = new MethodArgument();
                methodArgument.setName(parameterNames[i]);
                methodArgument.setArgumentIndex(i);
                methodArgument.setMethod(method);
                methodArgument.setAnnotationList(CollUtil.newHashSet(parameters[i].getAnnotations()));
                methodArgument.setArgumentType(parameters[i].getType());
                argument.put(parameterNames[i], methodArgument);
            }
        }

        return methodMeta;
    }
}
