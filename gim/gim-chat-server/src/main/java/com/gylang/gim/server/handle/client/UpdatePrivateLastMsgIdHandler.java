package com.gylang.gim.server.handle.client;

import com.gylang.gim.api.domain.admin.AdminUser;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.server.config.AdminConfig;
import com.gylang.gim.server.service.HistoryMessageService;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.IMRequestHandler;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 更新个人的聊天最新消息msgId 用户手动拉取消息的时候方便进行
 *
 * @author gylang
 * data 2021/3/18
 */
@Component
@NettyHandler(ChatType.PRIVATE_CHAT_LAST_MSG_ID)
public class UpdatePrivateLastMsgIdHandler implements IMRequestHandler, InitializingBean {

    @Autowired
    private HistoryMessageService historyMessageService;
    @Autowired
    private MessageProvider messageProvider;
    @Autowired
    private AdminConfig adminConfig;

    private Set<Map.Entry<String, AdminUser>> adminUser;

    @Override
    public Object process(GIMSession me, MessageWrap message) {

        historyMessageService.updatePrivateLastMsgId(me.getAccount(), message.getMsgId());
        for (Map.Entry<String, AdminUser> userEntry : adminUser) {
            MessageWrap messageWrap = message.copyBasic();
            messageWrap.setReceive(userEntry.getValue().getUserId());
            messageProvider.sendMsg(me, messageWrap.getReceive(), message);
        }
        return ReplyMessage.success(message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        adminUser = adminConfig.getAdminUser()
                .entrySet().stream()
                .filter(u -> u.getValue().isPersistence())
                .collect(Collectors.toSet());
    }
}
