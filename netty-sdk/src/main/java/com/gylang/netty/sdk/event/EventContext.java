package com.gylang.netty.sdk.event;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息通知上下文, 所有的消息通过 MessageContext进行中转或者发送
 *
 * @author gylang
 * data 2020/11/7
 * @version v0.0.1
 */
public class EventContext implements AfterConfigInitialize {

    /** 事件监听列表 */
    private final Map<String, List<MessageEventListener<?>>> eventListenerMap;


    public EventContext() {
        this.eventListenerMap = new HashMap<>();
    }

    /**
     * 消息监听器注册
     *
     * @param receive 监听器
     */
    public void register(MessageEventListener<?> receive) {

        // 解析类型
        for (Method method : receive.getClass().getDeclaredMethods()) {
            if ("onEvent".equals(method.getName()) && !method.isBridge()) {
                MessageEvent annotation = method.getAnnotation(MessageEvent.class);
                if (null != annotation) {
                    String[] value = annotation.value();
                    for (String msgType : value) {
                        List<MessageEventListener<?>> messageEventListenerList = eventListenerMap.computeIfAbsent(msgType, k -> new ArrayList<>());
                        messageEventListenerList.add(receive);
                    }
                }
            }
        }

    }


    /**
     * 通知
     *
     * @param key 消息key
     * @param msg 消息内容
     */
    public void sendMsg(String key, Object msg) {

        List<MessageEventListener<?>> messageNotifies = eventListenerMap.get(key);
        if (null != messageNotifies) {
            for (MessageEventListener<?> messageEventListener : messageNotifies) {

                ((MessageEventListener<Object>) messageEventListener).onEvent(key, msg);
            }
        }
    }


    @Override
    public void init(NettyConfiguration configuration) {
        if (CollUtil.isNotEmpty(configuration.getMessageEventListener())) {
            for (MessageEventListener<?> messageEventListener : configuration.getMessageEventListener()) {
                register(messageEventListener);
            }


        }
    }
}
