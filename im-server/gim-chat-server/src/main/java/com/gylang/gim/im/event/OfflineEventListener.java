package com.gylang.gim.im.event;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.im.constant.ReceiveType;
import com.gylang.gim.im.service.HistoryMessageService;
import com.gylang.netty.sdk.constant.EventTypeConst;
import com.gylang.netty.sdk.constant.MessageType;
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
