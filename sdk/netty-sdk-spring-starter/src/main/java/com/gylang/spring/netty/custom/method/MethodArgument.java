package com.gylang.spring.netty.custom.method;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Data
public class MethodArgument {

    /**
     * 参数名
     */
    private String name;
    /**
     * 参数类型
     */
    private Class<?> argumentType;
    /**
     * 注解
     */
    private Set<Annotation> annotationList;
    /**
     * 参数下标
     */
    private int argumentIndex;
    /**
     * 方法对象
     */
    private Method method;
    /**
     * 所属类
     */
    private Class<?> clazz;
    /**
     * 实例对象
     */
    private Object instance;

}
