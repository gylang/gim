package com.gylang.spring.netty.custom.method;

import cn.hutool.core.util.ReflectUtil;
import com.gylang.spring.netty.custom.handler.ControllerMethodMeta;
import lombok.Getter;

import java.util.Map;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Getter
public class MethodArgumentValue {

    private ControllerMethodMeta controllerMethodMeta;

    private Object[] parameter;

    public void init(ControllerMethodMeta controllerMethodMeta) {
        this.controllerMethodMeta = controllerMethodMeta;
        Map<String, MethodArgument> argument = controllerMethodMeta.getArgument();
        if (null == argument) {
            parameter = null;
        } else {
            parameter = new Object[argument.size()];
        }
    }

    public Object invoke() {

        return ReflectUtil.invoke(controllerMethodMeta.getInstance(), controllerMethodMeta.getMethod(), parameter);
    }

    public void pushIfNullParameter(int index, Object argument) {
        if (null == argument) {
            return;
        }
        pushParameter(index, argument);
    }

    public void pushParameter(int index, Object argument) {

        parameter[index] = argument;
    }
}
