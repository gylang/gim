package com.gylang.netty.sdk.event.message;

import java.lang.annotation.*;

/**
 * 消息通知注解 用于标识消息类型
 *
 * @author gylang
 * data 2020/11/7
 * @version v0.0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageEvent {

    String[] value();
}
