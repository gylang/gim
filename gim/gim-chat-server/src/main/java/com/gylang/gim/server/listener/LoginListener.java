package com.gylang.gim.server.listener;

import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.admin.AdminUser;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.server.config.AdminConfig;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.provider.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2021/4/16
 */
@Component
@Slf4j
public class LoginListener implements MessageEventListener<String>, InitializingBean {

    @Autowired
    private AdminConfig adminConfig;
    @Autowired
    private MessageProvider messageProvider;

    private Set<Map.Entry<String, AdminUser>> adminUser;

    @Override
    @MessageEvent(EventTypeConst.USER_ONLINE)
    public void onEvent(String key, String loginUserId) {


        // 用户上线通知
        for (Map.Entry<String, AdminUser> adminUserEntry : adminUser) {
            AdminUser user = adminUserEntry.getValue();
            MessageWrap messageWrap = new MessageWrap();
            messageWrap.setQos(2);
            messageWrap.setCmd(EventTypeConst.USER_ONLINE);
            messageWrap.setType(ChatTypeEnum.NOTIFY_CHAT);
            messageWrap.setContent(loginUserId);
            String sender = user.getUserId();
            messageWrap.setReceive(sender);
            messageWrap.setSender(CommonConstant.SYSTEM_SENDER);
            GIMSession session = new GIMSession();
            session.setAccount(CommonConstant.SYSTEM_SENDER);
            if (log.isDebugEnabled()) {
                log.debug("接收到用户[{}]上线事件, 给服务[{}]发送通知", key, user.getName());
            }
            messageProvider.sendMsg(session, sender, messageWrap);

        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {

        adminUser = adminConfig.getAdminUser()
                .entrySet().stream()
                .filter(u -> u.getValue().isLoginEvent())
                .collect(Collectors.toSet());
    }
}
