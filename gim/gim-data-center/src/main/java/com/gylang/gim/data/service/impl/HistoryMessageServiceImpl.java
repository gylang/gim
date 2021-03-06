package com.gylang.gim.data.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gylang.cache.CacheManager;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.ContentType;
import com.gylang.gim.api.constant.cmd.PushChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.domain.push.PushMessage;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.util.MsgIdUtil;
import com.gylang.gim.data.common.mybatis.Page;
import com.gylang.gim.data.entity.HistoryGroupChat;
import com.gylang.gim.data.entity.HistoryPrivateChat;
import com.gylang.gim.data.service.HistoryGroupChatService;
import com.gylang.gim.data.service.HistoryMessageService;
import com.gylang.gim.data.service.HistoryPrivateChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2021/3/21
 */
@Service
@Slf4j
public class HistoryMessageServiceImpl implements HistoryMessageService, ChatType {

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

    @Value("${gim.privateChat.history.oncePushMsgNum:50}")
    private Long priOncePushMsgNum;
    @Value("${gim.groupChat.history.oncePushMsgNum:50}")
    private Long groupOncePushMsgNum;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SocketManager socketManager;

    @Override
    public void updatePrivateLastMsgId(String uid, String msgId) {

        // ???????????? ????????????hash ????????? ?????????
        String cacheKey = getCacheKey(uid);
        // todo ?????????????????? msgId ??????, ???????????? ????????????????????????
        cacheManager.setMapField(cacheKey, uid, msgId);


    }

    private String getCacheKey(String uid) {
        long target = slot & Long.parseLong(uid);
        return CacheConstant.LAST_MSG_ID + target;
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

        ZSetOperations<String, String> hashOperations = redisTemplate.<String, String>opsForZSet();
        if (PRIVATE_CHAT == msg.getType()) {
            // ????????????
            HistoryPrivateChat privateChat = new HistoryPrivateChat();
            privateChat.setMessage(msg.getContent());
            privateChat.setReceive(msg.getReceive());
            privateChat.setMsgId(msg.getMsgId());
            privateChat.setSendId(msg.getSender());
            privateChat.setTimeStamp(MsgIdUtil.timestamp(msg.getTimeStamp()));
            historyPrivateChatService.save(privateChat);

            //  ?????????????????? ?????????
            String msgStr = JSON.toJSONString(msg);
            // ??????
            hashOperations.add(CacheConstant.CHAT_HISTORY + msg.getReceive(), msgStr, privateChat.getTimeStamp());
            // ??????
            hashOperations.add(CacheConstant.CHAT_HISTORY + msg.getSender(), msgStr, privateChat.getTimeStamp());
            if (log.isDebugEnabled()) {
                log.debug("[??????????????????] ????????????: ????????????: ?????????={},?????????={},  ?????????{}", msg.getSender(), msg.getReceive(), msg);
            }
            // ??????
        } else if (GROUP_CHAT == msg.getType()) {
            // ????????????
            HistoryGroupChat groupChat = new HistoryGroupChat();
            PushMessage groupMessage = JSON.parseObject(msg.getContent(), PushMessage.class);
            String message = groupMessage.getContent();
            groupChat.setMessage(message);
            groupChat.setReceive(msg.getReceive());
            groupChat.setMsgId(msg.getMsgId());
            groupChat.setSendId(msg.getSender());
            groupChat.setTimeStamp(MsgIdUtil.timestamp(Long.parseLong(msg.getMsgId())));
            historyGroupChatService.save(groupChat);
            //  ?????????????????? ?????????
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {

                StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                // ??????
                Long timeStamp = groupChat.getTimeStamp();
                stringRedisConnection.zAdd(CacheConstant.CHAT_HISTORY + msg.getSender(), timeStamp, message);
                // ??????
                for (String receive : groupMessage.getReceiveId()) {
                    stringRedisConnection.zAdd(CacheConstant.CHAT_HISTORY + receive, timeStamp, message);
                }
                return null;
            });
            if (log.isDebugEnabled()) {
                log.debug("[??????????????????] ????????????: ????????????: ?????????={},?????????={},  ?????????{}", msg.getSender(), groupMessage.getReceiveId(), msg);
            }
        }
    }

    @Override
    public void pushHistory(String key) {

        // ??????????????? ??????????????????
        String cacheKey = getCacheKey(key);
        String priMsgId = redisTemplate.<String, String>opsForHash()
                .get(cacheKey, key);
        // ????????????????????????
        if (null != priMsgId) {
            Long priLastTimeStamp = MsgIdUtil.timestamp(Long.parseLong(priMsgId)) - 1;
            batchPush(CacheConstant.CHAT_HISTORY + key, priLastTimeStamp, priOncePushMsgNum);
        } else {
            log.error("[??????????????????] ??????id = {},  lastMsgId??????", key);
        }

    }

    private void batchPush(String key, Long score, Long count) {


        while (true) {
            int offset = 0;

            Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet()
                    .rangeByScoreWithScores(key, score, Long.MAX_VALUE, offset, count);
            // ????????????
            if (CollUtil.isEmpty(typedTuples)) {
                log.info("[??????????????????] :??????id = {}, ??????????????? = {}", key, offset);
                return;
            }
            List<String> msgStr = typedTuples.stream()
                    .map(ZSetOperations.TypedTuple::getValue)
                    .collect(Collectors.toList());

            PushMessage message = new PushMessage();
            message.setContent(JSON.toJSONString(msgStr));
            message.setReceiveId(CollUtil.newArrayList(key));
            MessageWrap messageWrap = MessageWrap.builder()
                    .cmd(PushChatCmd.P2P_PUSH)
                    .contentType(ContentType.BATCH)
                    .content(JSON.toJSONString(msgStr))
                    .offlineMsgEvent(false)
                    .qos(1)
                    .build();
            // ??????
            offset += msgStr.size();
            socketManager.send(messageWrap);
            if (msgStr.size() < count) {
                log.info("[??????????????????] :??????id = {}, ??????????????? = {}", key, offset);
                return;
            }

        }


    }

}
