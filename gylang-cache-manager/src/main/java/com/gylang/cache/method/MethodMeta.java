package com.gylang.cache.method;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author gylang
 * data 2021/2/2
 */
@Data
public class MethodMeta {

    /** 实例对象 */
    private Object instance;
    /** 实例类型 */
    private Class<?> clazz;
    /** 方法 */
    private Method method;
    /** 参数 */
    private Object[] parameter;

}
