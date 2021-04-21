package com.gylang.gim.admin.event;


import java.util.List;

/**
 * @author gylang
 * data 2020/11/7
 * @version v0.0.1
 */
public interface MessageEventListener<T> {

    /**
     * 消息通知
     *
     * @param key 消息类型
     * @param m   消息类容
     */
    void onEvent(String key, T m);

    /**
     * 绑定的监听事件
     *
     * @return
     */
    List<String> bind();

}
