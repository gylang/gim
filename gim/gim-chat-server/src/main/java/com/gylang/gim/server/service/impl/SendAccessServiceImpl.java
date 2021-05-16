package com.gylang.gim.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.domain.manager.WhiteBlackList;
import com.gylang.gim.server.service.SendAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * @author gylang
 * data 2021/3/27
 */
@Service
public class SendAccessServiceImpl implements SendAccessService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
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

    @Override
    public WhiteBlackList queryPrivateInfo(WhiteBlackList whiteBlackList) {

        WhiteBlackList reslut = new WhiteBlackList();
        String key = CacheConstant.IM_USER_CONFIG + whiteBlackList.getId();
        String type = redisTemplate.opsForValue().get(key);
        reslut.setType(type);

        // 白名单
        key = CacheConstant.WHITE_LIST_CHECK + whiteBlackList.getId();
        Set<String> white = redisTemplate.opsForSet().members(key);
        reslut.setAddBlack(CollUtil.newArrayList(white));

        // 黑名单
        key = CacheConstant.BLACK_LIST_CHECK + whiteBlackList.getId();
        Set<String> black = redisTemplate.opsForSet().members(key);
        reslut.setAddBlack(CollUtil.newArrayList(black));

        return reslut;
    }

    @Override
    public void updatePrivate(WhiteBlackList whiteBlackList) {
        if (StrUtil.isNotEmpty(whiteBlackList.getType())) {
            // 黑白名单策略变更
            String key = CacheConstant.IM_USER_CONFIG + whiteBlackList.getId();
            redisTemplate.opsForValue().set(key, whiteBlackList.getType());
        }

        if (CollUtil.isNotEmpty(whiteBlackList.getAddWhite())) {
            // 新增白名单用户
            String key = CacheConstant.WHITE_LIST_CHECK + whiteBlackList.getId();
            redisTemplate.opsForSet().add(key, whiteBlackList.getAddWhite().toArray(new String[0]));
        }
        if (CollUtil.isNotEmpty(whiteBlackList.getAddBlack())) {
            // 新增黑名单用户
            String key = CacheConstant.BLACK_LIST_CHECK + whiteBlackList.getId();
            redisTemplate.opsForSet().add(key, whiteBlackList.getAddBlack().toArray(new String[0]));
        }
        if (CollUtil.isNotEmpty(whiteBlackList.getRemoveWhite())) {
            // 删除白名单用户
            String key = CacheConstant.WHITE_LIST_CHECK + whiteBlackList.getId();
            redisTemplate.opsForSet().remove(key, whiteBlackList.getRemoveWhite().toArray());
        }
        if (CollUtil.isNotEmpty(whiteBlackList.getRemoveBlack())) {
            // 删除黑名单用户
            String key = CacheConstant.BLACK_LIST_CHECK + whiteBlackList.getId();
            redisTemplate.opsForSet().remove(key, whiteBlackList.getRemoveBlack().toArray());
        }
    }

    @Override
    public WhiteBlackList queryGroupInfo(WhiteBlackList whiteBlackList) {
        WhiteBlackList reslut = new WhiteBlackList();
        String key = CacheConstant.GROUP_CHAT_CONFIG + whiteBlackList.getId();
        String type = redisTemplate.opsForValue().get(key);
        reslut.setType(type);

        // 白名单
        key = CacheConstant.GROUP_CAN_SEND_MSG_CHECK + whiteBlackList.getId();
        Set<String> white = redisTemplate.opsForSet().members(key);
        reslut.setAddBlack(CollUtil.newArrayList(white));

        // 黑名单
        key = CacheConstant.GROUP_DISABLE_SEND_MSG_CHECK + whiteBlackList.getId();
        Set<String> black = redisTemplate.opsForSet().members(key);
        reslut.setAddBlack(CollUtil.newArrayList(black));

        return reslut;
    }

    @Override
    public void updateGroup(WhiteBlackList whiteBlackList) {
        if (StrUtil.isNotEmpty(whiteBlackList.getType())) {
            // 黑白名单策略变更
            String key = CacheConstant.GROUP_CHAT_CONFIG + whiteBlackList.getId();
            redisTemplate.opsForValue().set(key, whiteBlackList.getType());
        }

        if (CollUtil.isNotEmpty(whiteBlackList.getAddWhite())) {
            // 新增白名单用户
            String key = CacheConstant.GROUP_CAN_SEND_MSG_CHECK + whiteBlackList.getId();
            redisTemplate.opsForSet().add(key, whiteBlackList.getAddWhite().toArray(new String[0]));
        }
        if (CollUtil.isNotEmpty(whiteBlackList.getAddBlack())) {
            // 新增黑名单用户
            String key = CacheConstant.GROUP_DISABLE_SEND_MSG_CHECK + whiteBlackList.getId();
            redisTemplate.opsForSet().add(key, whiteBlackList.getAddBlack().toArray(new String[0]));
        }
        if (CollUtil.isNotEmpty(whiteBlackList.getRemoveWhite())) {
            // 删除白名单用户
            String key = CacheConstant.GROUP_CAN_SEND_MSG_CHECK + whiteBlackList.getId();
            redisTemplate.opsForSet().remove(key, whiteBlackList.getRemoveWhite().toArray());
        }
        if (CollUtil.isNotEmpty(whiteBlackList.getRemoveBlack())) {
            // 删除黑名单用户
            String key = CacheConstant.GROUP_DISABLE_SEND_MSG_CHECK + whiteBlackList.getId();
            redisTemplate.opsForSet().remove(key, whiteBlackList.getRemoveBlack().toArray());
        }

    }
}
