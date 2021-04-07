package com.gylang.gim.server.remote;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.server.domain.AdminUser;
import com.gylang.gim.server.config.AdminConfig;
import com.gylang.netty.sdk.constant.system.SystemRemoteType;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
import com.gylang.spring.netty.annotation.SpringNettyHandler;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author gylang
 * data 2021/4/7
 */
@SpringNettyHandler(SystemRemoteType.REMOTE_LOGIN)
public class RemoteLoginHandler implements IMRequestHandler {

    @Resource
    private AdminConfig adminConfig;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        AdminUser user = JSON.parseObject(message.getContent(), AdminUser.class);
        Map<String, AdminUser> adminUser = adminConfig.getAdminUser();
        AdminUser login = adminUser.get(user.getUsername());
        if (null == login) {
        // 用户不存在
            return null;
        }
        if (login.getPassword().equals(user.getPassword())) {
            // 验证成功
        }
        me.setAccount(login.getUserId());
        LocalSessionHolderUtil.set(login.getUserId(), me.getSession());
        return null;
    }
}
