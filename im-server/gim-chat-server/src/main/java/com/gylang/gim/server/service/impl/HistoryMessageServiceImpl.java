package com.gylang.gim.server.service.impl;

import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.server.service.HistoryMessageService;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.util.MsgIdUtil;
import lombok.extern.slf4j.Slf4j;
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


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${gylang.netty.privateMsgSlot:3}")
    private Integer slot;

    @Value("${gylang.netty.serverIndex:0}")
    private Integer serverIndex;

    @Override
    public void updatePrivateLastMsgId(String uid, String msgId) {

        // 用户量大 可以使用hash 先分组 再记录
        redisTemplate.opsForHash().put(CacheConstant.LAST_MSG_ID + uid, "-1", msgId);
    }

    @Override
    public void updateGroupLastMsgIdHandler(String groupId, String uid, String msgId) {

        redisTemplate.opsForHash().put(CacheConstant.LAST_MSG_ID + uid, groupId, msgId);

    }

    @Override
    public void storePrivateChat(String uid, MessageWrap message) {
        redisTemplate.opsForZSet().add(CacheConstant.PRIVATE_CHAT_HISTORY + uid,
                message,
                message.getTimeStamp());
    }

    @Override
    public void storeGroupChat(String groupId, MessageWrap message) {

        // 通过sort set 将消息放入缓存
        redisTemplate.opsForZSet().add(CacheConstant.GROUP_CHAT_HISTORY + message.getReceive(),
                message,
                message.getTimeStamp());
    }


    @Override
    public Long privateHistoryId(String msgId) {
        return MsgIdUtil.getTimestamp(Long.parseLong(msgId));
    }

    @Override
    public Long groupHistoryId(String msgId) {
        return MsgIdUtil.getTimestamp(Long.parseLong(msgId));
    }
}
