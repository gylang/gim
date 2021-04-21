package com.gylang.gim.web.event;

import com.gylang.netty.sdk.constant.EventTypeConst;
import com.gylang.gim.api.domain.common.MessageWrap;
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
public class CrossServerPushEvent implements MessageEventListener<MessageWrap> {

    @Override
    @MessageEvent(EventTypeConst.CROSS_SERVER_PUSH)
    public void onEvent(String key, MessageWrap message) {

        log.info("收到跨服我消息，准备通过桥接、mq发送嘻嘻奥 ： {}", message);
    }
}
