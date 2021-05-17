package com.gylang.gim.room;

import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.event.message.MessageEvent;
import com.gylang.netty.sdk.api.event.message.MessageEventListener;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import com.gylang.netty.sdk.api.repo.GIMSessionRepository;
import com.gylang.netty.sdk.api.util.LocalSessionHolderUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author gylang
 * data 2021/5/17
 */
@Component
public class OfflineEvent implements MessageEventListener<GIMSession> {

    @Resource
    private GIMGroupSessionRepository groupSessionRepository;
    @Resource
    private GIMSessionRepository sessionRepository;
    @Resource
    private MessageProvider messageProvider;

    @Override
    @MessageEvent({EventTypeConst.OVER_TIME_CLOSE, EventTypeConst.QOS_SEND_MES_ERROR, EventTypeConst.COLSE_CONNECT})
    public void onEvent(String key, GIMSession session) {

        String account = session.getAccount();
        LocalSessionHolderUtil.remove(account);
        groupSessionRepository.removeMember("1", account);
        sessionRepository.removeByUserId(account);
        Set<String> allMemberIds = groupSessionRepository.getAllMemberIds("1");
        for (String allMemberId : allMemberIds) {
            MessageWrap messageWrap = new MessageWrap();
            messageWrap.setContent(account + ", 退出群聊");
            messageWrap.setType(ChatType.CHAT_ROOM);
            messageWrap.setSender(CommonConstant.SYSTEM_SENDER);
            GIMSession gimSession = new GIMSession();
            gimSession.setAccount(CommonConstant.SYSTEM_SENDER);
            messageProvider.sendMsg(gimSession, allMemberId, messageWrap);
        }
    }
}
