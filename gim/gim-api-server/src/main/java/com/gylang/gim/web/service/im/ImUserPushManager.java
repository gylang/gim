package com.gylang.gim.web.service.im;

import com.gylang.gim.api.domain.push.PushMessage;

/**
 * @author gylang
 * data 2021/5/17
 */
public interface ImUserPushManager {


    /**
     * 推送消息
     *
     * @param content         推送内容
     * @param qos             qos
     * @param offlineMsgEvent 是否持久化
     */
    void push(String content, int qos, boolean offlineMsgEvent);

    /**
     * 推送消息
     *
     * @param content         推送内容
     * @param qos             qos
     * @param offlineMsgEvent 是否持久化
     * @param receive         推送消息接收人
     * @param send            推送消息发送人
     * @param type            推送消息类型
     * @
     */
    void push(String send, int type, boolean offlineMsgEvent, String content, int qos, String... receive);

    /**
     * 推送消息
     *
     * @param content 推送内容
     * @param qos     qos
     * @param msg     推送的消息
     */
    void push(int qos, PushMessage msg);
}
