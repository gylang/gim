package com.gylang.netty.sdk.event;

import com.gylang.netty.sdk.common.AfterConfigInitialize;

/**
 * 事件发送器 对外只暴露发送方法
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public interface EventProvider extends AfterConfigInitialize {
    /**
     * 通知
     *
     * @param key     消息key
     * @param message 消息内容
     */
    void sendEvent(String key, Object message);

    /**
     * 异步通知
     *
     * @param key     消息key
     * @param message 消息内容
     */
    void sendAsyncEvent(String key, Object message);
}
