package com.gylang.chat;

import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;

/**
 * @author gylang
 * data 2020/11/11
 * @version v0.0.1
 */
public class CloseMonitor implements MessageEventListener<String> {



    @Override
    @MessageEvent("test")
    public void onEvent(String key, String message) {

    }
}
