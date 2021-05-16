package com.gylang.netty.sdk.api.annotation;

import java.lang.annotation.*;

/**
 * 业务处理注解
 *
 * @author gylang
 * data 2020/11/1
 * @version v0.0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NettyHandler {
    /**
     * 处理类型名称
     */
    int value() default Integer.MAX_VALUE;
}
