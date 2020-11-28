package com.gylang.spring.netty.custom.method;

import cn.hutool.core.util.ReflectUtil;
import com.gylang.spring.netty.custom.handler.MethodMeta;
import lombok.Getter;

import java.util.Map;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Getter
public class MethodArgumentValue {

    private MethodMeta methodMeta;

    private Object[] parameter;

    public void init(MethodMeta methodMeta) {
        this.methodMeta = methodMeta;
        Map<String, MethodArgument> argument = methodMeta.getArgument();
        if (null == argument) {
            parameter = null;
        } else {
            parameter = new Object[argument.size()];
        }
    }

    public Object invoke() {

        return ReflectUtil.invoke(methodMeta.getInstance(), methodMeta.getMethod(), parameter);
    }

    public void pushParameter(int index, Object argument) {
        parameter[index] = argument;
    }
}
