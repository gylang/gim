package com.gylang.gim.server.handle.manager.impl;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.biztype.ManagerType;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.entity.UserCache;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.server.handle.manager.ManagerService;
import com.gylang.netty.sdk.domain.model.IMSession;
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
    public MessageWrap doInvoke(IMSession session, MessageWrap messageWrap) {


        // 生成接入令牌
        UserCache userCache = JSON.parseObject(messageWrap.getContent(), UserCache.class);

        redisTemplate.opsForValue().set(userCache.getToken(), userCache.getId(), userCache.getExpire(), TimeUnit.SECONDS);

        // 保存用户数据
        redisTemplate.opsForValue().set(CacheConstant.USER + userCache.getId(), userCache);

        return ReplyMessage.success(messageWrap);
    }

    @Override
    public String managerType() {
        return ManagerType.USER_APPLY_FOR_TOKEN_MANAGER;
    }
}
