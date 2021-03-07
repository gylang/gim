package com.gylang.im.im.event;

import com.alibaba.fastjson.JSON;
import com.gylang.im.dao.entity.HistoryGroupChat;
import com.gylang.im.dao.entity.HistoryNotifyChat;
import com.gylang.im.dao.entity.HistoryPrivateChat;
import com.gylang.im.im.constant.ReceiveType;
import com.gylang.im.web.service.HistoryGroupChatService;
import com.gylang.im.web.service.HistoryNotifyChatService;
import com.gylang.im.web.service.HistoryPrivateChatService;
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
    private HistoryPrivateChatService historyPrivateChatService;
    @Resource
    private HistoryGroupChatService historyGroupChatService;
    @Resource
    private HistoryNotifyChatService historyNotifyChatService;

    @Override
    @MessageEvent(EventTypeConst.OFFLINE_MSG_EVENT)
    public void onEvent(String key, MessageWrap message) {

        // 分类离线消息
        if (null != message) {

            // 拼装入库消息
            if (MessageType.NOTIFY == message.getType()) {
                HistoryNotifyChat chat = new HistoryNotifyChat();
                chat.setMsgId(message.getMsgId());
                chat.setSendId(message.getSender());
                chat.setTargetId(message.getTargetId());
                chat.setTimeStamp(message.getTimeStamp());
                chat.setMessage(JSON.toJSONString(message));
                historyNotifyChatService.save(chat);
            } else if (ReceiveType.RECEIVE_IS_PRIVATE == message.getReceiverType()) {

                HistoryPrivateChat chat = new HistoryPrivateChat();
                chat.setMsgId(message.getMsgId());
                chat.setSendId(message.getSender());
                chat.setTargetId(message.getTargetId());
                chat.setTimeStamp(message.getTimeStamp());
                chat.setMessage(JSON.toJSONString(message));
                historyPrivateChatService.save(chat);
            } else if (ReceiveType.RECEIVE_IS_GROUP == message.getReceiverType()) {
                HistoryGroupChat chat = new HistoryGroupChat();
                chat.setMsgId(message.getMsgId());
                chat.setSendId(message.getSender());
                chat.setTargetId(message.getTargetId());
                chat.setTimeStamp(message.getTimeStamp());
                chat.setMessage(JSON.toJSONString(message));
                historyGroupChatService.save(chat);
            }
        }
    }
}
