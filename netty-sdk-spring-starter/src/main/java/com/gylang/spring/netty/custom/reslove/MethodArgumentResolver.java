package com.gylang.spring.netty.custom.reslove;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.annotation.NettyMapping;
import com.gylang.netty.sdk.common.MethodWrap;
import com.gylang.netty.sdk.common.ParameterWrap;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Component
public class MethodArgumentResolver implements AbstractResolver<Method, MethodWrap> {


    @Override
    public boolean support(Method method) {
        return method.isAnnotationPresent(NettyMapping.class);
    }

    @Override
    public MethodWrap resolve(Method method) {

        // 方法处理器 用户存储方法信息 和 参数信息
        MethodWrap methodWrap = new MethodWrap();
        methodWrap.setMethod(method);
        methodWrap.addAnno(AnnotationUtils.findAnnotation(method, NettyMapping.class));


        // 解析参数
        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        if (null != parameterNames && null != parameters && parameterNames.length == parameters.length) {
            List<ParameterWrap> parameterWrapList = new ArrayList<>();
            methodWrap.setParameterWrapList(parameterWrapList);
            for (int i = 0; i < parameterNames.length; i++) {
                ParameterWrap parameterWrap = new ParameterWrap();
                parameterWrap.setName(parameterNames[i]);
                parameterWrap.setType(parameters[i].getType());
                parameterWrap.setAnnotationList(CollUtil.newArrayList(parameters[i].getAnnotations()));
                parameterWrapList.add(parameterWrap);
            }
        }

        return methodWrap;
    }
}
