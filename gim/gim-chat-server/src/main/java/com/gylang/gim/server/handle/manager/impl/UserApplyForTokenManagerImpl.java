package com.gylang.gim.server.handle.manager.impl;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.dto.response.LoginResponse;
import com.gylang.gim.server.handle.manager.ManagerService;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author gylang
 * data 2021/5/6
 */
@Service
public class UserApplyForTokenManagerImpl implements ManagerService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public MessageWrap doInvoke(GIMSession session, MessageWrap messageWrap) {


        // 生成接入令牌
        LoginResponse loginResponse = JSON.parseObject(messageWrap.getContent(), LoginResponse.class);

        redisTemplate.opsForValue().set(loginResponse.getImToken(), loginResponse.getUid(), loginResponse.getImExpire(), TimeUnit.SECONDS);

        // 保存用户数据
//        redisTemplate.opsForValue().set(CacheConstant.USER + loginResponse.getUid(), JSON.toJSONString(loginResponse));

        return ReplyMessage.success(messageWrap);
    }

    @Override
    public String managerType() {
        return ManagerCmd.USER_APPLY_FOR_TOKEN_MANAGER;
    }
}
