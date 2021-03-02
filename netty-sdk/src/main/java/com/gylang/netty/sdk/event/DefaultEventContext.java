package com.gylang.netty.sdk.event;

import com.gylang.netty.sdk.event.message.MessageEventListener;

import java.util.List;

/**
 * 继承MessageContext 简化消息订阅的初始化方式
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
public class DefaultEventContext extends EventContext {

    public void init(List<MessageEventListener<?>> messageEventListenerList) {
        if (null == messageEventListenerList) {
            return;
        }
        for (MessageEventListener<?> messageEventListener : messageEventListenerList) {
            register(messageEventListener);
        }
    }


}
