package com.gylang.gim.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gylang.cache.CacheManager;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.util.MsgIdUtil;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.entity.HistoryGroupChat;
import com.gylang.gim.web.entity.HistoryPrivateChat;
import com.gylang.gim.web.service.HistoryGroupChatService;
import com.gylang.gim.web.service.HistoryMessageService;
import com.gylang.gim.web.service.HistoryPrivateChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author gylang
 * data 2021/3/21
 */
@Service
@Slf4j
public class HistoryMessageServiceImpl implements HistoryMessageService, ChatTypeEnum {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private HistoryPrivateChatService historyPrivateChatService;
    @Autowired
    private HistoryGroupChatService historyGroupChatService;

    @Value("${gylang.netty.privateMsgSlot:3}")
    private Integer slot;

    @Value("${gylang.netty.serverIndex:0}")
    private Integer serverIndex;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void updatePrivateLastMsgId(String uid, String msgId) {

        // 用户量大 可以使用hash 先分组 再记录
        cacheManager.setMapField(CacheConstant.LAST_MSG_ID + uid, "-1", msgId);


    }

    @Override
    public void updateGroupLastMsgIdHandler(String groupId, String uid, String msgId) {

        cacheManager.setMapField(CacheConstant.LAST_MSG_ID + uid, groupId, msgId);

    }

    @Override
    public PageResponse<HistoryPrivateChat> privateHistory(Page<HistoryPrivateChat> page, String uid) {

        long targetSlot = Long.parseLong(uid) & (slot - 1);
        long lastId = cacheManager.mapGet(CacheConstant.LAST_MSG_ID + targetSlot, uid);
        historyPrivateChatService.page(page, Wrappers.<HistoryPrivateChat>lambdaQuery()
                .ge(true, HistoryPrivateChat::getId, lastId * 100));
        return page.toDTO(HistoryPrivateChat.class);
    }

    @Override
    public PageResponse<HistoryGroupChat> groupHistory(Page<HistoryGroupChat> page, String uid) {

        long lastId = cacheManager.mapGet(CacheConstant.LAST_MSG_ID + uid, page.getParam().getReceive());
        historyGroupChatService.page(page, Wrappers.<HistoryGroupChat>lambdaQuery()
                .ge(true, HistoryGroupChat::getId, lastId * 100));
        return page.toDTO(HistoryGroupChat.class);
    }

    @Override
    public Long privateHistoryId(String msgId) {
        return MsgIdUtil.resolveUUID(msgId) * 100 + serverIndex;
    }

    @Override
    public Long groupHistoryId(String msgId) {
        return null;
    }


    @Override
    public void store(MessageWrap msg) {

        HashOperations<String, String, String> hashOperations = redisTemplate.<String, String>opsForHash();
        if (PRIVATE_CHAT == msg.getType()) {
            // 私聊入库
            HistoryPrivateChat privateChat = new HistoryPrivateChat();
            privateChat.setMessage(msg.getContent());
            privateChat.setReceive(msg.getReceive());
            privateChat.setMsgId(msg.getMsgId());
            privateChat.setSendId(msg.getSender());
            privateChat.setTimeStamp(MsgIdUtil.getTimestamp(msg.getTimeStamp()));
            historyPrivateChatService.save(privateChat);

            //  投入缓存信箱 写扩散
            String msgStr = JSON.toJSONString(msg);
            // 收箱
            hashOperations.put(CacheConstant.LAST_MSG_ID + msg.getReceive(), msg.getSender(), msgStr);
            // 发箱
            hashOperations.put(CacheConstant.LAST_MSG_ID + msg.getSender(), msg.getReceive(), msgStr);
            // 发箱
        } else if (GROUP_CHAT == msg.getType()) {
            // 群聊入库
            HistoryGroupChat groupChat = new HistoryGroupChat();
            groupChat.setMessage(msg.getContent());
            groupChat.setReceive(msg.getReceive());
            groupChat.setMsgId(msg.getMsgId());
            groupChat.setSendId(msg.getSender());
            groupChat.setTimeStamp(MsgIdUtil.getTimestamp(msg.getTimeStamp()));
            historyGroupChatService.save(groupChat);
            String msgStr = JSON.toJSONString(msg);
            //  投入缓存信箱 写扩散

            // 发箱
            hashOperations.put(CacheConstant.LAST_MSG_ID + msg.getSender(), msg.getReceive(), msgStr);
            // 收箱

        }
        //

    }
}
