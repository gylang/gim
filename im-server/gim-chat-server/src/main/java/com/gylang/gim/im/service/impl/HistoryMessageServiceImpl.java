package com.gylang.gim.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.gylang.cache.CacheManager;
import com.gylang.gim.im.constant.CacheConstant;
import com.gylang.gim.im.service.HistoryMessageService;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.util.MsgIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/21
 */
@Service
@Slf4j
public class HistoryMessageServiceImpl implements HistoryMessageService {

    @Autowired
    private CacheManager cacheManager;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${gylang.netty.privateMsgSlot:3}")
    private Integer slot;

    @Value("${gylang.netty.serverIndex:0}")
    private Integer serverIndex;

    @Override
    public void updatePrivateLastMsgId(String uid, String msgId) {

        // 用户量大 可以使用hash 先分组 再记录
        long targetSlot = Long.parseLong(uid) & (slot - 1);
        cacheManager.setMapField(CacheConstant.PRIVATE_LAST_MSG_ID + targetSlot, uid, msgId);
    }

    @Override
    public void updateGroupLastMsgIdHandler(String groupId, String uid, String msgId) {

        cacheManager.setMapField(CacheConstant.GROUP_LAST_MSG_ID + groupId, String.valueOf(uid), msgId);

    }

    @Override
    public void storePrivateChat(String uid, MessageWrap message) {
        redisTemplate.opsForZSet().add(CacheConstant.PRIVATE_CHAT_HISTORY + uid,
                message,
                message.getTimeStamp());
    }

    @Override
    public void storeGroupChat(String groupId,  MessageWrap message) {

        redisTemplate.opsForZSet().add(CacheConstant.GROUP_CHAT_HISTORY + message.getReceive(),
                message,
                message.getTimeStamp());
    }


    @Override
    public Long privateHistoryId(String msgId) {
        return MsgIdUtil.resolveUUID(msgId) * 100 + serverIndex;
    }

    @Override
    public Long groupHistoryId(String msgId) {
        return null;
    }
}
