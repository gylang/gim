package com.gylang.netty.sdk.event.message;



/**
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
     * @see MessageEvent
     */
    void onEvent(String key, T message);


}
