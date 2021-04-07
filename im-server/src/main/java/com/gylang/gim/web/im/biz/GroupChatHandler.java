package com.gylang.gim.web.im.biz;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.web.dao.entity.HistoryGroupChat;
import com.gylang.gim.web.dto.msg.ErrorMessageWrap;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.web.service.HistoryGroupChatService;
import com.gylang.gim.web.service.ImUserGroupService;
import com.gylang.gim.web.service.UserConfigService;
import com.gylang.gim.web.im.constant.BizChatCmd;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.util.MsgIdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/5
 */
@Component
@NettyHandler(BizChatCmd.GROUP_CHAT)
public class GroupChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;

    @Resource
    private UserConfigService userConfigService;

    @Resource
    private ImUserGroupService imUserGroupService;
    @Resource
    private HistoryGroupChatService historyGroupChatService;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        // 1. 好友关系校验
        if (imUserGroupService.checkIsYouGroup(me.getAccount(), message.getReceive())) {
            // 发送消息
            // 直接备份消息
            HistoryGroupChat groupChat = new HistoryGroupChat();
            MsgIdUtil.increase(message);
            groupChat.setMsgId(message.getMsgId());
            groupChat.setMessage(JSON.toJSONString(message));
            groupChat.setSendId(me.getAccount());
            groupChat.setTimeStamp(message.getTimeStamp());
            historyGroupChatService.save(groupChat);
            // 发送群消息
            messageProvider.sendGroup(me, message.getReceive(), message.copyBasic());
            return null;
        } else {
            // 错误提示
            ErrorMessageWrap messageWrap = ErrorMessageWrap
                    .build(BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE.getCode(), "你不是该群聊成员, 无法发送!");
            messageWrap.setOfflineMsgEvent(false);
            return messageWrap;
        }
    }
}
