package com.gylang.gim.admin.event;

import com.gylang.gim.admin.config.NettyConfiguration;
import lombok.Data;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息发送器
 *
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
@Data
public class DefaultEventProvider implements EventProvider {
    /**
     * 异步任务线程池
     */
    private ThreadPoolExecutor executor;
    /**
     * 消息上下文
     */
    private EventContext eventContext;

    @Override
    public void sendEvent(String key, Object message) {
        eventContext.sendMsg(key, message);
    }

    @Override
    public void sendAsyncEvent(String key, Object message) {
        executor.execute(() -> eventContext.sendMsg(key, message));
    }

    @Override
    public void init(NettyConfiguration configuration) {
        this.executor = configuration.getPoolExecutor();
        this.eventContext = configuration.getEventContext();
    }
}
