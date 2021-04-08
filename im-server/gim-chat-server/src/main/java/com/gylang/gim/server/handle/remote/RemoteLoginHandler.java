package com.gylang.gim.server.handle.remote;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.CommonConstant;
import com.gylang.gim.api.constant.cmd.AdminChatCmd;
import com.gylang.gim.api.constant.cmd.PrivateChatCmd;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.server.config.AdminConfig;
import com.gylang.gim.server.domain.AdminUser;
import com.gylang.gim.server.domain.ResponseMessageWrap;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author gylang
 * data 2021/4/7
 */
@Component
@NettyHandler(AdminChatCmd.REMOTE_LOGIN)
public class RemoteLoginHandler implements IMRequestHandler {

    @Resource
    private AdminConfig adminConfig;

    @Override
    public Object process(IMSession me, MessageWrap message) {


        MessageWrap messageWrap = new ResponseMessageWrap();
        messageWrap.setSender(me.getAccount());
        messageWrap.setCmd(PrivateChatCmd.SOCKET_CONNECTED);
        messageWrap.setType(ChatTypeEnum.NOTIFY.getType());

        AdminUser user = JSON.parseObject(message.getContent(), AdminUser.class);
        Map<String, AdminUser> adminUser = adminConfig.getAdminUser();
        AdminUser login = adminUser.get(user.getUsername());
        if (null == login) {
            // 用户不存在
            messageWrap.setContent("授权失败，请验证你的用户密码");
            messageWrap.setCode(CommonConstant.FALSE_INT_STR);
            return messageWrap;
        }
        if (user.getPassword().equals(login.getPassword())) {
            // 验证成功
            me.setAccount(login.getUserId());
            LocalSessionHolderUtil.set(login.getUserId(), me.getSession());
            messageWrap.setCode(BaseResultCode.OK.getCode());
            return messageWrap;
        }
        messageWrap.setContent("授权失败，请验证你的用户密码");
        return messageWrap;
    }
}
