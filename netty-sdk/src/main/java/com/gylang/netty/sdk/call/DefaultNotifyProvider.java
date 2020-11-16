package com.gylang.netty.sdk.call;

import lombok.Data;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息发送器
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
@Data
public class DefaultNotifyProvider implements NotifyProvider {
    /**
     * 异步任务线程池
     */
    private ThreadPoolExecutor executor;
    /**
     * 消息上下文
     */
    private NotifyContext messageContext;

    @Override
    public void sendMsg(String key, Object message) {
        messageContext.sendMsg(key, message);
    }

    @Override
    public void sendAsyncMsg(String key, Object message) {
        executor.execute(() -> {
            messageContext.sendMsg(key, message);
        });
    }
}
