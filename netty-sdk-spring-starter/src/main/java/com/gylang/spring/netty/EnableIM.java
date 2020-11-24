package com.gylang.spring.netty;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author gylang
 * data 2020/11/16
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(IMAutoConfigration.class)
public @interface EnableIM {
}
