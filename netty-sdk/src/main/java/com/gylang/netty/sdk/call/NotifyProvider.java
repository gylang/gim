package com.gylang.netty.sdk.call;

/**
 * 消息发送器 对外只暴露发送方法
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public interface NotifyProvider {
    /**
     * 通知
     *
     * @param key     消息key
     * @param message 消息内容
     */
    void sendMsg(String key, Object message);

    /**
     * 异步通知
     *
     * @param key     消息key
     * @param message 消息内容
     */
    void sendAsyncMsg(String key, Object message);
}
