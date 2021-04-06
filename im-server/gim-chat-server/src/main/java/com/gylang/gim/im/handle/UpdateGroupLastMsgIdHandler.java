package com.gylang.gim.im.handle;

import com.gylang.im.api.constant.cmd.PrivateChatCmd;
import com.gylang.gim.im.service.HistoryMessageService;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
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
        return null;
    }
}
