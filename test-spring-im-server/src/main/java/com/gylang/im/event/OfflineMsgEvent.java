package com.gylang.im.event;

import com.gylang.netty.sdk.constant.EventTypeConst;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2021/3/3
 */
@Component
@Slf4j
public class OfflineMsgEvent implements MessageEventListener<MessageWrap> {


    @Override
    @MessageEvent(EventTypeConst.OFFLINE_MSG_EVENT)
    public void onEvent(String key, MessageWrap message) {
      log.info("收到离线消息请求 ： {}", message);
    }
}
