package com.gylang.gim.server.handle.client;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.common.ResponseMessage;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.server.service.HistoryMessageService;
import com.gylang.gim.server.service.SendAccessService;
import com.gylang.netty.sdk.annotation.NettyHandler;
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
@NettyHandler(ChatTypeEnum.PRIVATE_CHAT)
public class PrivateChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;
    @Resource
    private HistoryMessageService messageService;
    @Resource
    private SendAccessService sendAccessService;

    @Override
    public Object process(IMSession me, MessageWrap message) {


        boolean access = sendAccessService.privateAccessCheck(me.getAccount(), message.getReceive());

        // 发送消息
        if (access) {

            int res = messageProvider.sendMsg(me, message.getReceive(), message.copyBasic());
            if (!MessageProvider.USER_NOT_FOUND.equals(res)) {
                return ResponseMessage.copy(message);
            } else {
                return ReplyMessage.reply(message, BaseResultCode.VISIT_INTERCEPT.getCode(), "用户不存在");
            }

        }
        return ReplyMessage.reply(message, BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE);
    }
}
