package com.gylang.gim.im.biz;

import com.gylang.gim.im.constant.cmd.PrivateChatCmd;
import com.gylang.gim.im.domain.AckMessageWrap;
import com.gylang.gim.im.domain.ResponseMessageWrap;
import com.gylang.gim.im.service.HistoryMessageService;
import com.gylang.gim.im.service.SendAccessService;
import com.gylang.im.common.enums.BaseResultCode;
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
@NettyHandler(PrivateChatCmd.SIMPLE_PRIVATE_CHAT)
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
            // 将新消息存入 redis 刷入记录
            if (message.isStore()) {
                messageService.storePrivateChat(message.getReceive(), message);
            }
            messageProvider.sendMsg(me, message.getReceive(), message.copyBasic());
            return ResponseMessageWrap.copy(message);

        }
        AckMessageWrap ackMessageWrap = new AckMessageWrap(message);
        ackMessageWrap.setCode(BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE.getCode());
        ackMessageWrap.setContent(BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE.getMsg());
        return ackMessageWrap;
    }
}
