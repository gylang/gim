package com.gylang.netty.sdk.provider;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.GIMSession;

/**
 * @author gylang
 * data 2021/5/14
 */
public interface CrossMessageHandler {

    /**
     * 发送跨服信息
     * @param host 服务
     * @param sender  发送方
     * @param message 消息体
     */
    void sendMsg(String host, GIMSession sender, MessageWrap message);
}
