package com.gylang.gim.room;

import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.BaseSessionGroup;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.IMRequestHandler;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import com.gylang.netty.sdk.api.repo.GIMSessionRepository;
import com.gylang.netty.sdk.api.util.LocalSessionHolderUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author gylang
 * data 2021/5/17
 */
@Component
@NettyHandler(ChatType.CLIENT_AUTH)
public class JoinRoomHandler implements IMRequestHandler, InitializingBean {

    @Resource
    private GIMGroupSessionRepository groupSessionRepository;
    @Resource
    private GIMSessionRepository sessionRepository;
    @Resource
    private MessageProvider messageProvider;


    @Override
    public Object process(GIMSession me, MessageWrap message) {

        String content = message.getContent();
        me.setAccount(content);

        if (Boolean.TRUE.equals(groupSessionRepository.checkIsMember("1", content))) {
            return ReplyMessage.reply(message, BaseResultCode.USERNAME_EXIST);
        }
        groupSessionRepository.addMember("1", content);
        sessionRepository.addSession(me);
        LocalSessionHolderUtil.set(content, me.getSession());

        // 加入通知
        Set<String> allMemberIds = groupSessionRepository.getAllMemberIds("1");
        for (String allMemberId : allMemberIds) {
            MessageWrap messageWrap = message.copyBasic();
            messageWrap.setType(ChatType.CHAT_ROOM);
            messageWrap.setContent(content + ", 加入群聊");
            messageWrap.setSender(CommonConstant.SYSTEM_SENDER);
            GIMSession gimSession = new GIMSession();
            gimSession.setAccount(CommonConstant.SYSTEM_SENDER);
            messageProvider.sendMsg(gimSession, allMemberId, messageWrap);
        }

        return ReplyMessage.success(message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建聊天室群组
        BaseSessionGroup sessionGroup = new BaseSessionGroup();
        sessionGroup.setGroupId("1");
        groupSessionRepository.add(sessionGroup);
    }
}
