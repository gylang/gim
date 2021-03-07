package com.gylang.im.im.chat;

import com.gylang.im.web.dto.msg.ErrorMessageWrap;
import com.gylang.im.common.enums.BaseResultCode;
import com.gylang.im.web.service.ImUserGroupService;
import com.gylang.im.web.service.UserConfigService;
import com.gylang.im.im.constant.BizChatCmd;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/5
 */
@Component
@NettyHandler(BizChatCmd.GROUP_CHAT)
public class GroupChat implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;

    @Resource
    private UserConfigService userConfigService;

    @Resource
    private ImUserGroupService imUserGroupService;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        // 1. 好友关系校验
        if (imUserGroupService.checkIsYouGroup(me.getAccount(), message.getTargetId())) {
            // 发送消息
            messageProvider.sendGroup(me, message.getReceive(), message.copyBasic());
            return null;
        } else {
            ErrorMessageWrap messageWrap = ErrorMessageWrap
                    .build(BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE.getCode(), "你不是该群聊成员, 无法发送!");
            messageWrap.setOfflineMsgEvent(false);
            return messageWrap;
        }
    }
}
