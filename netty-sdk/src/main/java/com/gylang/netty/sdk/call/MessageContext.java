package com.gylang.netty.sdk.call;

import com.gylang.netty.sdk.call.message.MessageNotify;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息通知上下文, 所有的消息通过 MessageContext进行中转或者发送
 * @author gylang
 * data 2020/11/7
 * @version v0.0.1
 */
public  class MessageContext {

    Map<String, List<MessageNotify<?>>> notifyMap;

    Map<Class<?>, Class<?>> messageType;



    public MessageContext() {
        this.messageType = new HashMap<>();
        this.notifyMap = new HashMap<>();
    }

    /**
     * 消息监听器注册
     *
     * @param receive 监听器
     */
    public void register(MessageNotify<?> receive) {

        // 解析类型
        for (Method method : receive.getClass().getDeclaredMethods()) {
            if ("msgNotify".equals(method.getName()) && !method.isBridge()) {
                CallMessage annotation = method.getAnnotation(CallMessage.class);
                if (null != annotation) {
                    String[] value = annotation.value();
                    for (String msgType : value) {
                        List<MessageNotify<?>> messageNotifyList = notifyMap.computeIfAbsent(msgType, k -> new ArrayList<>());
                        messageNotifyList.add(receive);
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

        List<MessageNotify<?>> messageNotifies = notifyMap.get(key);
        if (null != messageNotifies) {
            for (MessageNotify<?> messageNotify : messageNotifies) {

                Class<?> msgType = messageType.get(messageNotify.getClass());
                if (msgType.isInstance(msg)) {
                    ((MessageNotify<Object>) messageNotify).msgNotify(key, msg);
                }
            }
        }
    }



}
