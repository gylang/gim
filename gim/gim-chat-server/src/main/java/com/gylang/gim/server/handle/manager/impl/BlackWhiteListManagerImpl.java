package com.gylang.gim.server.handle.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.mamager.BlackWhiteListConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.manager.WhiteBlackList;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.server.handle.manager.ManagerService;
import com.gylang.netty.sdk.domain.model.GIMSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author gylang
 * data 2021/5/15
 */
@Service
public class BlackWhiteListManagerImpl implements ManagerService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public MessageWrap doInvoke(GIMSession session, MessageWrap messageWrap) {

        WhiteBlackList whiteBlackList = JSON.parseObject(messageWrap.getContent(), WhiteBlackList.class);

        if (StrUtil.isNotEmpty(whiteBlackList.getType())) {
            // 黑白名单策略变更
            String key = CacheConstant.IM_USER_CONFIG + whiteBlackList.getUid();
            redisTemplate.opsForValue().set(key, whiteBlackList.getType());
        }

        if (BlackWhiteListConstant.WHITE_ADD.equals(messageWrap.getCode())) {
            // 新增白名单用户
            String key = CacheConstant.WHITE_LIST_CHECK + whiteBlackList.getUid();
            redisTemplate.opsForSet().add(key, whiteBlackList.getAddWhite().toArray(new String[0]));
        }
        if (BlackWhiteListConstant.BLACK_ADD.equals(messageWrap.getCode())) {
            // 新增黑名单用户
            String key = CacheConstant.BLACK_LIST_CHECK + whiteBlackList.getUid();
            redisTemplate.opsForSet().add(key, whiteBlackList.getAddBlack().toArray(new String[0]));
        }
        if (BlackWhiteListConstant.WHITE_REMOVE.equals(messageWrap.getCode())) {
            // 删除白名单用户
            String key = CacheConstant.WHITE_LIST_CHECK + whiteBlackList.getUid();
            redisTemplate.opsForSet().remove(key, whiteBlackList.getRemoveWhite().toArray());
        }
        if (BlackWhiteListConstant.BLACK_REMOVE.equals(messageWrap.getCode())) {
            // 删除黑名单用户
            String key = CacheConstant.BLACK_LIST_CHECK + whiteBlackList.getUid();
            redisTemplate.opsForSet().remove(key, whiteBlackList.getRemoveBlack().toArray());
        }

        return ReplyMessage.success(messageWrap);
    }

    @Override
    public String managerType() {
        return ManagerCmd.BLACK_WHITE_LIST_MANAGER;
    }
}
