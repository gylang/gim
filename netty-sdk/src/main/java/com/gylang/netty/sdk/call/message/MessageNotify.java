package com.gylang.netty.sdk.call.message;


/**
 * @author gylang
 * data 2020/11/7
 * @version v0.0.1
 */
public interface MessageNotify<T> {

    /**
     * 消息通知
     *
     * @param key     消息类型
     * @param message 消息类容
     * @see com.gylang.netty.sdk.call.CallMessage
     */
    void msgNotify(String key, T message);


}
