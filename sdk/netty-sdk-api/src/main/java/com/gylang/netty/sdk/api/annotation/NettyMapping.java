package com.gylang.netty.sdk.api.annotation;

import java.lang.annotation.*;

/**
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NettyMapping {

    int value();
}
