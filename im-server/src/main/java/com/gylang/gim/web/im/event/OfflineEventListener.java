package com.gylang.gim.web.im.event;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.web.dao.entity.HistoryGroupChat;
import com.gylang.gim.web.dao.entity.HistoryNotifyChat;
import com.gylang.gim.web.dao.entity.HistoryPrivateChat;
import com.gylang.gim.web.im.constant.ReceiveType;
import com.gylang.gim.web.service.HistoryMessageService;
import com.gylang.gim.web.service.HistoryGroupChatService;
import com.gylang.gim.web.service.HistoryNotifyChatService;
import com.gylang.gim.web.service.HistoryPrivateChatService;
import com.gylang.netty.sdk.constant.EventTypeConst;
import com.gylang.netty.sdk.constant.system.SystemMessageType;
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
    @Resource
    private HistoryMessageService historyMessageService;

    @Override
    @MessageEvent(EventTypeConst.OFFLINE_MSG_EVENT)
    public void onEvent(String key, MessageWrap message) {

        // 分类离线消息
        if (null != message) {

            // 拼装入库消息
            if (SystemMessageType.NOTIFY.equals(message.getCmd())) {
                HistoryNotifyChat chat = new HistoryNotifyChat();
                chat.setMsgId(message.getMsgId());
                chat.setSendId(message.getSender());
                chat.setTimeStamp(message.getTimeStamp());
                chat.setMessage(JSON.toJSONString(message));
                historyNotifyChatService.save(chat);
            } else if (ReceiveType.RECEIVE_IS_PRIVATE == message.getReceiverType()) {

                HistoryPrivateChat chat = new HistoryPrivateChat();
                historyMessageService.privateHistoryId(message.getMsgId());
                chat.setMsgId(message.getMsgId());
                chat.setSendId(message.getSender());
                chat.setTimeStamp(message.getTimeStamp());
                chat.setMessage(JSON.toJSONString(message));
                historyPrivateChatService.save(chat);
            } else if (ReceiveType.RECEIVE_IS_GROUP == message.getReceiverType()) {
                HistoryGroupChat chat = new HistoryGroupChat();
                historyMessageService.groupHistoryId(message.getMsgId());
                chat.setMsgId(message.getMsgId());
                chat.setSendId(message.getSender());
                chat.setTimeStamp(message.getTimeStamp());
                chat.setMessage(JSON.toJSONString(message));
                historyGroupChatService.save(chat);
            }
        }
    }
}
