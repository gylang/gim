package com.gylang.gim.server.handle.client;

import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.server.service.HistoryMessageService;
import com.gylang.gim.api.constant.cmd.PrivateChatCmd;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新个人群组中的最新消息msgId
 *
 * @author gylang
 * data 2021/3/18
 */
@Component
@NettyHandler(PrivateChatCmd.GROUP_CHAT_LAST_MSG_ID)
public class UpdateGroupLastMsgIdHandler implements IMRequestHandler {

    @Autowired
    private HistoryMessageService historyMessageService;

    @Override
    public Object process(IMSession me, MessageWrap message) {
        historyMessageService.updateGroupLastMsgIdHandler(message.getReceive(), me.getAccount(), message.getMsgId());
        if (QosConstant.ONE_AWAY != message.getQos()) {
            return ReplyMessage.success(message);
        }
        return null;
    }
}
