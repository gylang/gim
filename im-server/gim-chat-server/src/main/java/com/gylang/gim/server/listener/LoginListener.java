package com.gylang.gim.server.listener;

import com.gylang.gim.api.constant.CommonConstant;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.admin.AdminUser;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.server.config.AdminConfig;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.provider.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gylang
 * data 2021/4/16
 */
@Component
@Slf4j
public class LoginListener implements MessageEventListener<String> {

    @Autowired
    private AdminConfig adminConfig;
    @Autowired
    private MessageProvider messageProvider;

    @Override
    @MessageEvent(EventTypeConst.USER_ONLINE)
    public void onEvent(String key, String loginUserId) {

        // 用户上线通知
        Map<String, AdminUser> adminUser = adminConfig.getAdminUser();
        for (Map.Entry<String, AdminUser> adminUserEntry : adminUser.entrySet()) {
            AdminUser user = adminUserEntry.getValue();
            if (user.isLoginEvent()) {
                MessageWrap messageWrap = new MessageWrap();
                messageWrap.setQos(2);
                messageWrap.setCmd(EventTypeConst.USER_ONLINE);
                messageWrap.setType(ChatTypeEnum.NOTIFY);
                messageWrap.setContent(loginUserId);
                String sender = user.getUserId();
                messageWrap.setReceive(sender);
                messageWrap.setSender(CommonConstant.SYSTEM_SENDER);
                IMSession session = new IMSession();
                session.setAccount(CommonConstant.SYSTEM_SENDER);
                if (log.isDebugEnabled()) {
                    log.debug("接收到用户[{}]上线事件, 给服务[{}]发送通知", key, user.getName());
                }
                messageProvider.sendMsg(session, loginUserId, messageWrap);
            }

        }

    }
}
