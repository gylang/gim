package com.gylang.netty.sdk.api.event.message;

import java.lang.annotation.*;

/**
 * 消息事件触发通知注解 用于标识消息类型
 *
 * @author gylang
 * data 2020/11/7
 * @version v0.0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageEvent {

    /**
     * 事件类型
     * @return 事件类型
     */
    String[] value();
}
