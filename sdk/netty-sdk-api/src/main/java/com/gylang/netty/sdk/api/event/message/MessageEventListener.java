package com.gylang.netty.sdk.api.event.message;


/**
 * 消息事件监听器 监听事件触发
 *
 * @author gylang
 * data 2020/11/7
 * @version v0.0.1
 */
public interface MessageEventListener<T> {

    /**
     * 消息通知
     *
     * @param key     消息类型
     * @param message 消息类容
     * @see MessageEvent 方法注解 标识事件类型
     */
    void onEvent(String key, T message);


}
