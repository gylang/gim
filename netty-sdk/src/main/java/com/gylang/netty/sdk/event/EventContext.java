package com.gylang.netty.sdk.event;

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
public class EventContext {
    /** 事件监听列表 */
    private Map<String, List<MessageEventListener<?>>> notifyMap;
    /** 消息类型 */
    Map<Class<?>, Class<?>> messageType;


    public EventContext() {
        this.messageType = new HashMap<>();
        this.notifyMap = new HashMap<>();
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
                        List<MessageEventListener<?>> messageEventListenerList = notifyMap.computeIfAbsent(msgType, k -> new ArrayList<>());
                        messageEventListenerList.add(receive);
                    }
                    // 判断入消息入参
                    messageType.put(receive.getClass(), method.getParameterTypes()[1]);
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

        List<MessageEventListener<?>> messageNotifies = notifyMap.get(key);
        if (null != messageNotifies) {
            for (MessageEventListener<?> messageEventListener : messageNotifies) {

                Class<?> msgType = messageType.get(messageEventListener.getClass());
                if (msgType.isInstance(msg)) {
                    ((MessageEventListener<Object>) messageEventListener).onEvent(key, msg);
                }
            }
        }
    }


}
