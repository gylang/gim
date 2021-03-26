package com.gylang.gim.im.biz;

import com.gylang.gim.im.constant.BizChatCmd;
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


    @Override
    public Object process(IMSession me, MessageWrap message) {

        // 1. 好友关系校验

        // 2. 是否开启非常勿扰
        // 发送消息
        messageProvider.sendMsg(me, message.getTargetId(), message.copyBasic());

        return null;
    }
}
