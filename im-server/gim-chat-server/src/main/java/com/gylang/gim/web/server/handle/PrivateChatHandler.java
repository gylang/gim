package com.gylang.gim.web.server.handle;

import com.gylang.gim.web.api.constant.cmd.PrivateChatCmd;
import com.gylang.gim.web.server.domain.AckMessageWrap;
import com.gylang.gim.web.server.domain.ResponseMessageWrap;
import com.gylang.gim.web.server.service.HistoryMessageService;
import com.gylang.gim.web.server.service.SendAccessService;
import com.gylang.gim.web.api.enums.BaseResultCode;
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

            int res = messageProvider.sendMsg(me, message.getReceive(), message.copyBasic());
            if (!MessageProvider.USER_NOT_FOUND.equals(res)) {
                // 将新消息存入 redis 刷入记录
                if (message.isStore()) {
                    messageService.storePrivateChat(message.getReceive(), message);
                }
                return ResponseMessageWrap.copy(message);
            } else {
                AckMessageWrap ackMessageWrap = new AckMessageWrap(message);
                ackMessageWrap.setCode(BaseResultCode.VISIT_INTERCEPT.getCode());
                ackMessageWrap.setContent("用户不存在");
                return ackMessageWrap;
            }


        }
        AckMessageWrap ackMessageWrap = new AckMessageWrap(message);
        ackMessageWrap.setCode(BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE.getCode());
        ackMessageWrap.setContent(BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE.getMsg());
        return ackMessageWrap;
    }
}
