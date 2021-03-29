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
@NettyHandler(BizChatCmd.GROUP_CHAT)
public class GroupChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;


    @Override
    public Object process(IMSession me, MessageWrap message) {

        // 1. 好友关系校验

        // 发送群消息
        messageProvider.sendMsg(me, message.getReceive(), message.copyBasic());
        return null;
    }
}
