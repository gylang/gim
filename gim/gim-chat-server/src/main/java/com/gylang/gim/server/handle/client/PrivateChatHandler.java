package com.gylang.gim.server.handle.client;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.server.service.HistoryMessageService;
import com.gylang.gim.server.service.SendAccessService;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.IMRequestHandler;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/5
 */
@Component
@NettyHandler(ChatType.PRIVATE_CHAT)
public class PrivateChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;
    @Resource
    private HistoryMessageService messageService;
    @Resource
    private SendAccessService sendAccessService;

    @Override
    public Object process(GIMSession me, MessageWrap message) {


        boolean access = sendAccessService.privateAccessCheck(me.getAccount(), message.getReceive());

        // 发送消息
        if (access) {

            int res = messageProvider.sendMsg(me, message.getReceive(), message.copyBasic());
            if (MessageProvider.SENDING.equals(res)) {
                return ReplyMessage.reply(message, BaseResultCode.OK);
            } else if (MessageProvider.USER_OFFLINE.equals(res)) {
                return ReplyMessage.reply(message, BaseResultCode.USER_OFFLINE);
            } else {
                return ReplyMessage.reply(message, BaseResultCode.VISIT_INTERCEPT.getCode(), "用户不存在");
            }

        }
        return ReplyMessage.reply(message, BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE);
    }
}
