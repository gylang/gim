package com.gylang.netty.sdk.api.provider;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.domain.model.GIMSession;

/**
 * @author gylang
 * data 2021/5/14
 */
public interface CrossMessageProvider {

    /**
     * 发送跨服信息
     *
     * @param host    服务
     * @param sender  发送方
     * @param message 消息体
     */
    void sendMsg(String host, GIMSession sender, MessageWrap message);


    /**
     * 接收消息
     *
     * @param sender  发送方
     * @param message 接收方
     */
    void receive(GIMSession sender, MessageWrap message);
}
