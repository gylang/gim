package com.gylang.im.im.biz;

import com.gylang.im.common.constant.CommonConstant;
import com.gylang.im.common.enums.BaseResultCode;
import com.gylang.im.dao.entity.UserConfig;
import com.gylang.im.im.constant.BizChatCmd;
import com.gylang.im.web.dto.msg.ErrorMessageWrap;
import com.gylang.im.web.service.ImUserFriendService;
import com.gylang.im.web.service.UserConfigService;
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
        if (userFriendService.checkIsYouFriend(me.getAccount(), message.getTargetId())) {
            // 发送消息
            messageProvider.sendMsg(me, message.getTargetId(), message.copyBasic());

        }

        UserConfig userConfig = userConfigService.findUserConfig(message.getTargetId());
        if (null == userConfig
                || CommonConstant.TRUE_INT == userConfig.getReceiveStranger()) {
            // 2. 是否开启非常勿扰
            // 发送消息
            messageProvider.sendMsg(me, message.getTargetId(), message.copyBasic());

        } else {
            ErrorMessageWrap messageWrap = ErrorMessageWrap
                    .build(BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE.getCode(), "对方拒绝收到默认人消息");
            messageWrap.setOfflineMsgEvent(false);
            return messageWrap;
        }
        return null;
    }
}
