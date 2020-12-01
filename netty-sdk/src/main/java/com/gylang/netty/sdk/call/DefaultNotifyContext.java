package com.gylang.netty.sdk.call;

import com.gylang.netty.sdk.call.message.MessageNotifyListener;

import java.util.List;

/**
 * 继承MessageContext 简化消息订阅的初始化方式
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
public class DefaultNotifyContext extends NotifyContext {

    public void init(List<MessageNotifyListener<?>> messageNotifyListenerList) {
        if (null == messageNotifyListenerList) {
            return;
        }
        for (MessageNotifyListener<?> messageNotifyListener : messageNotifyListenerList) {
            register(messageNotifyListener);
        }
    }


}
