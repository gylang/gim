package com.gylang.netty.sdk.annotation;

import com.gylang.netty.sdk.handler.DefaultAdapterDispatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gylang
 * data 2020/11/11
 * @version v0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdapterType {

    /**
     * 是否为适配器执行分发器
     * @see DefaultAdapterDispatch
     * @return 默认为否
     */
    boolean isDispatch() default false;

    /**
     * 适配器链式执行权重排序 0 ->1,2,3...N
     * @return 排序值
     */
    int order() default Integer.MAX_VALUE;
}
