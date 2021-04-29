package com.gylang.spring.netty;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 服务端IM 启动器
 * @author gylang
 * data 2020/11/16
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(NettyAutoConfiguration.class)
public @interface EnableIM {
}
