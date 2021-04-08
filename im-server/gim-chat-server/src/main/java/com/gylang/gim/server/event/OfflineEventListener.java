package com.gylang.gim.server.event;

import com.gylang.gim.server.service.HistoryMessageService;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/6
 */
@Component
public class OfflineEventListener implements MessageEventListener<MessageWrap> {


    @Resource
    private HistoryMessageService historyMessageService;

    @Override
    @MessageEvent(EventTypeConst.OFFLINE_MSG_EVENT)
    public void onEvent(String key, MessageWrap message) {


    }
}
