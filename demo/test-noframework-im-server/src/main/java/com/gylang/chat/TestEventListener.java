package com.gylang.chat;

import com.gylang.netty.sdk.api.event.message.MessageEvent;
import com.gylang.netty.sdk.api.event.message.MessageEventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gylang
 * data 2020/11/11
 * @version v0.0.1
 */
@Slf4j
public class TestEventListener implements MessageEventListener<String> {



    @Override
    @MessageEvent("test")
    public void onEvent(String key, String message) {
        log.info("接收到消息: message = " + message);
    }
}
