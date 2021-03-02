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

    /** 业务控制层元数据 */
    private ControllerMethodMeta controllerMethodMeta;
    /** 参数值 */
    private Object[] parameterValue;

    public void init(ControllerMethodMeta controllerMethodMeta) {
        this.controllerMethodMeta = controllerMethodMeta;
        Map<String, MethodArgument> argument = controllerMethodMeta.getArgument();
        if (null == argument) {
            parameterValue = null;
        } else {
            parameterValue = new Object[argument.size()];
        }
    }

    /**
     * 反射调用
     * @return 业务方法返回值
     */
    public Object invoke() {

        return ReflectUtil.invoke(controllerMethodMeta.getInstance(), controllerMethodMeta.getMethod(), parameterValue);
    }

    /**
     * 设置参数值
     * @param index 下标
     * @param argument 参数值
     */
    public void pushIfNullParameter(int index, Object argument) {
        if (null == argument) {
            return;
        }
        pushParameter(index, argument);
    }
    /**
     * 设置参数值
     * @param index 下标
     * @param argument 参数值
     */
    public void pushParameter(int index, Object argument) {

        parameterValue[index] = argument;
    }
}
