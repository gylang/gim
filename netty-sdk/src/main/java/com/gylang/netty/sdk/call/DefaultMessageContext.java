package com.gylang.netty.sdk.call;

import com.gylang.netty.sdk.call.message.MessageNotify;

import java.util.List;

/**
 * 继承MessageContext 简化消息订阅的初始化方式
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
public class DefaultMessageContext extends NotifyContext {

    public void init(List<MessageNotify<?>> messageNotifyList) {
        if (null == messageNotifyList) {
            return;
        }
        for (MessageNotify<?> messageNotify : messageNotifyList) {
            register(messageNotify);
        }
    }


}
