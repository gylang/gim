package com.gylang.gim.server.listener;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.domain.admin.AdminUser;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.server.config.AdminConfig;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.event.message.MessageEvent;
import com.gylang.netty.sdk.api.event.message.MessageEventListener;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 离线消息入库
 *
 * @author gylang
 * data 2021/3/6
 */
@Component
@Slf4j
public class PersistenceEventListener implements MessageEventListener<MessageWrap>, InitializingBean {

    @Autowired
    private AdminConfig adminConfig;
    @Autowired
    private MessageProvider messageProvider;

    private Set<Map.Entry<String, AdminUser>> adminUser;


    @Override
    @MessageEvent(EventTypeConst.PERSISTENCE_MSG_EVENT)
    public void onEvent(String key, MessageWrap message) {

        // 用户上线通知
        for (Map.Entry<String, AdminUser> adminUserEntry : adminUser) {
            AdminUser user = adminUserEntry.getValue();
            MessageWrap messageWrap = new MessageWrap();
            messageWrap.setQos(2);
            messageWrap.setCmd(EventTypeConst.PERSISTENCE_MSG_EVENT);
            messageWrap.setType(ChatType.NOTIFY_CHAT);
            MessageWrap history = message.copyBasic();
            history.setOfflineMsgEvent(false);
            messageWrap.setContent(JSON.toJSONString(history));
            String receive = user.getUserId();
            messageWrap.setReceive(receive);
            messageWrap.setSender(CommonConstant.SYSTEM_SENDER);
            GIMSession session = new GIMSession();
            session.setAccount(CommonConstant.SYSTEM_SENDER);
            if (log.isDebugEnabled()) {
                log.debug("接收到离线消息[{}], 给服务[{}]发送通知", key, user.getName());
            }
            messageProvider.sendMsg(session, receive, messageWrap);

        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {

        adminUser = adminConfig.getAdminUser()
                .entrySet().stream()
                .filter(u -> u.getValue().isPersistence())
                .collect(Collectors.toSet());
    }
}
