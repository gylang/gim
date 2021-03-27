package com.gylang.gim.im.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.gylang.gim.im.constant.CacheConstant;
import com.gylang.gim.im.constant.CommonConstant;
import com.gylang.gim.im.service.SendAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

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


        return redisTemplate.execute(wlcBlcCheck, Arrays.asList(configKey, blcKey, wlcKey, senderId));
    }
}
