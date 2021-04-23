package com.gylang.spring.netty.annotation;

import com.gylang.netty.sdk.annotation.NettyController;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gylang
 * data 2020/11/16
 * @version v0.0.1
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpringNettyController {



    @AliasFor(annotation = Component.class, attribute = "value")
    String name() default "";
}
