package com.gylang.gim.web.service.im.impl;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.GimPropertyConstant;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.mamager.UserManagerConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.entity.UserCache;
import com.gylang.gim.api.dto.response.LoginResponse;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.web.service.im.ImUserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author gylang
 * data 2021/5/16
 */
@Service
@ConditionalOnProperty(name = GimPropertyConstant.RUN_MODEL_KEY, havingValue = GimPropertyConstant.RUN_MODEL.STANDARD)
public class ImUserManagerImpl implements ImUserManager, InitializingBean {

    @Autowired
    private SocketManager socketManager;

    @Override
    public void addUser(UserCache userCache) {

        MessageWrap messageWrap = MessageWrap.builder()
                .qos(1)
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.USER_MANAGER)
                .code(UserManagerConstant.ADD_USER)
                .content(JSON.toJSONString(userCache))
                .build();
        socketManager.send(messageWrap);
    }

    @Override
    public void imUserAuth(LoginResponse loginResponse) {

        MessageWrap messageWrap = MessageWrap.builder()
                .qos(1)
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.USER_APPLY_FOR_TOKEN_MANAGER)
                .code(UserManagerConstant.DEL_USER)
                .content(JSON.toJSONString(loginResponse))
                .build();
        socketManager.send(messageWrap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        System.out.println();
    }
}
