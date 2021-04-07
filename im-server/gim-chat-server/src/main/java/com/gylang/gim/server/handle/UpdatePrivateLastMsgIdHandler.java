package com.gylang.gim.server.handle;

import com.gylang.gim.server.service.HistoryMessageService;
import com.gylang.gim.api.constant.cmd.PrivateChatCmd;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新个人的聊天最新消息msgId 用户手动拉取消息的时候方便进行
 *
 * @author gylang
 * data 2021/3/18
 */
@Component
@NettyHandler(PrivateChatCmd.PRIVATE_CHAT_LAST_MSG_ID)
public class UpdatePrivateLastMsgIdHandler implements IMRequestHandler {

    @Autowired
    private HistoryMessageService historyMessageService;


    @Override
    public Object process(IMSession me, MessageWrap message) {

        historyMessageService.updatePrivateLastMsgId(me.getAccount(), message.getMsgId());
        return null;
    }
}
