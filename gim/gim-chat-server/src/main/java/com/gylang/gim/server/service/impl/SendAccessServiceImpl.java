package com.gylang.gim.server.service.impl;

import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.server.service.SendAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author gylang
 * data 2021/3/27
 */
@Service
public class SendAccessServiceImpl implements SendAccessService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisScript<Boolean> wlcBlcCheck;


    @Override
    public Boolean privateAccessCheck(String senderId, String receiveId) {

        String configKey = CacheConstant.IM_USER_CONFIG + receiveId;
        String blcKey = CacheConstant.BLACK_LIST_CHECK + receiveId;
        String wlcKey = CacheConstant.WHITE_LIST_CHECK + receiveId;
        // lua 执行 判断用户是否启用白名单/黑名单 并对其判断是否在白名单/黑名单
        return redisTemplate.execute(wlcBlcCheck, Arrays.asList(configKey, blcKey, wlcKey, senderId));
    }

    @Override
    public Boolean groupAccessCheck(String senderId, String receiveId) {
        String configKey = CacheConstant.GROUP_CHAT_CONFIG + receiveId;
        String blcKey = CacheConstant.GROUP_DISABLE_SEND_MSG_CHECK + receiveId;
        String wlcKey = CacheConstant.GROUP_CAN_SEND_MSG_CHECK + receiveId;
        // lua 执行 判断用户是否启用白名单/黑名单 并对其判断是否在白名单/黑名单
        return redisTemplate.execute(wlcBlcCheck, Arrays.asList(configKey, blcKey, wlcKey, senderId));
    }
}
