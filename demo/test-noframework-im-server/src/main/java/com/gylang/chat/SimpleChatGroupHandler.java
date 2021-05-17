package com.gylang.chat;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.IMRequestHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
@NettyHandler(ChatType.GROUP_CHAT)
@Slf4j
public class SimpleChatGroupHandler implements IMRequestHandler {

    @Override
    public Object process(GIMSession me, MessageWrap message) {
        log.info("收到消息 : {}", message.getContent());
        log.info("发送test事件");
        NettyConfigHolder.getInstance().getEventProvider().sendEvent("test", "hahaha这是一个事件消息");
        NettyConfigHolder.getInstance().getMessageProvider().sendMsg(me, me, MessageWrap.builder()
                .cmd("ACK")
                .content("收到了")
                .build());
        return null;
    }
}
