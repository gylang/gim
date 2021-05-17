package com.gylang.gim.room;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.api.annotation.IMGroupRepository;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.IMRequestHandler;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author gylang
 * data 2021/5/17
 */
@Component
@NettyHandler(ChatType.CHAT_ROOM)
public class SingleRoomHandler implements IMRequestHandler {

    @Resource
    private GIMGroupSessionRepository groupRepository;
    @Resource
    private MessageProvider messageProvider;

    @Override
    public Object process(GIMSession me, MessageWrap message) {

        Set<String> allMemberIds = groupRepository.getAllMemberIds("1");

        for (String allMemberId : allMemberIds) {
            MessageWrap messageWrap = message.copyBasic();
            messageWrap.setType(ChatType.CHAT_ROOM);
            messageProvider.sendMsg(me, allMemberId, messageWrap);
        }
        return ReplyMessage.success(message);
    }
}
