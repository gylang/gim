package com.gylang.gim.server.handle.remote;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.domain.admin.AdminUser;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.server.config.AdminConfig;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.repo.GIMSessionRepository;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author gylang
 * data 2021/4/7
 */
@Component
@NettyHandler(ChatTypeEnum.REMOTE_LOGIN)
public class RemoteLoginHandler implements IMRequestHandler {

    @Resource
    private AdminConfig adminConfig;
    @Autowired
    private GIMSessionRepository sessionRepository;

    @Override
    public Object process(GIMSession me, MessageWrap message) {

        AdminUser user = JSON.parseObject(message.getContent(), AdminUser.class);
        Map<String, AdminUser> adminUser = adminConfig.getAdminUser();
        AdminUser login = adminUser.get(user.getUsername());
        if (null == login) {
            // 用户不存在
            return ReplyMessage.reply(message, BaseResultCode.USERNAME_PASSWORD_ERROR);
        }
        if (user.getPassword().equals(login.getPassword())) {
            // 验证成功
            me.setAccount(login.getUserId());
            LocalSessionHolderUtil.set(login.getUserId(), me.getSession());
            ReplyMessage success = ReplyMessage.success(message);
            success.setSender(me.getAccount());
            success.setQos(2);
            LocalSessionHolderUtil.set(login.getUserId(), me.getSession());
            sessionRepository.addSession(me);
            return success;
        }
        return ReplyMessage.reply(message, BaseResultCode.USERNAME_PASSWORD_ERROR);
    }
}
