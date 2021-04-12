package com.gylang.gim.web.im.biz;

import com.gylang.gim.web.common.constant.CommonConstant;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.web.dao.entity.UserConfig;
import com.gylang.gim.web.im.constant.BizChatCmd;
import com.gylang.gim.web.dto.msg.ErrorMessageWrap;
import com.gylang.gim.web.service.ImUserFriendService;
import com.gylang.gim.web.service.UserConfigService;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.gim.api.domain.common.MessageWrap;
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
@NettyHandler(BizChatCmd.PRIVATE_CHAT)
public class PrivateChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;

    @Resource
    private UserConfigService userConfigService;

    @Resource
    private ImUserFriendService userFriendService;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        // 1. 好友关系校验
        if (userFriendService.checkIsYouFriend(me.getAccount(), message.getReceive())) {
            // 发送消息
            messageProvider.sendMsg(me, message.getReceive(), message.copyBasic());

        }

        UserConfig userConfig = userConfigService.findUserConfig(message.getReceive());
        if (null == userConfig
                || CommonConstant.TRUE_INT == userConfig.getReceiveStranger()) {
            // 2. 是否开启非常勿扰
            // 发送消息
            messageProvider.sendMsg(me, message.getReceive(), message.copyBasic());

        } else {
            ErrorMessageWrap messageWrap = ErrorMessageWrap
                    .build(BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE.getCode(), "对方拒绝收到默认人消息");
            messageWrap.setOfflineMsgEvent(false);
            return messageWrap;
        }
        return null;
    }
}