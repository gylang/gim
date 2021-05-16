package com.gylang.gim.server.handle.manager.impl;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.mamager.UserManagerConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.entity.UserCache;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.server.handle.manager.ManagerService;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.repo.GIMSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/5/6
 */
@Service
@Slf4j
public class UserManagerImpl implements ManagerService {

    @Resource
    private GIMSessionRepository sessionRepository;

    @Override
    public MessageWrap doInvoke(GIMSession session, MessageWrap messageWrap) {

        UserCache userCache = JSON.parseObject(messageWrap.getContent(), UserCache.class);

        if (UserManagerConstant.ADD_USER.equals(messageWrap.getCode())) {
            // 新增用户
            GIMSession gimSession = new GIMSession();
            gimSession.setAccount(userCache.getId());
            sessionRepository.addSession(gimSession);
            log.info("[用户管理] : 新增用户 id = {}", userCache.getId());
        }

        return ReplyMessage.success(messageWrap);
    }

    @Override
    public String managerType() {
        return ManagerCmd.USER_MANAGER;
    }
}
