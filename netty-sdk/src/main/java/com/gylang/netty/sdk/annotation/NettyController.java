package com.gylang.netty.sdk.annotation;

import java.lang.annotation.*;

/**
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NettyController {

    int value() default Integer.MAX_VALUE;
}
