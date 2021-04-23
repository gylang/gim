package com.gylang.gim.web.event;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.admin.event.MessageEventListener;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.ContentType;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.constant.biztype.PushBizType;
import com.gylang.gim.api.constant.cmd.PushChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.push.PushMessage;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.util.MsgIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2021/4/16
 */
@Component
@Slf4j
public class UserLoginEvent implements MessageEventListener<MessageWrap> {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Value("${gim.privateChat.history.oncePushMsgNum:50}")
    private Long priOncePushMsgNum;
    @Value("${gim.groupChat.history.oncePushMsgNum:50}")
    private Long groupOncePushMsgNum;
    @Autowired
    private SocketManager socketManager;

    @Override
    public void onEvent(String key, MessageWrap m) {

        // 异步任务
        if (log.isDebugEnabled()) {
            log.debug("接收到用户[{}]上线通知, 准备推送离线消息", key);
        }
        CompletableFuture.runAsync(() -> {
            // 通过内置线程池处理
            // 获取用户msgId信箱

            Map<String, String> entries = redisTemplate.<String, String>opsForHash()
                    .entries(CacheConstant.LAST_MSG_ID + key);
            if (log.isDebugEnabled()) {
                log.debug("用户[{}]推送离线消息列表:{}", key, entries);
            }
            String priMsgId = entries.get("-1");
            // 推送单聊信箱消息
            Long priLastTimeStamp = MsgIdUtil.getTimestamp(Long.parseLong(priMsgId)) - 1;
            batchPush(CacheConstant.PRIVATE_CHAT_HISTORY + key, priLastTimeStamp, priOncePushMsgNum);

            // 群聊消息
            entries.remove("-1");
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                Long groupLastTimeStamp = MsgIdUtil.getTimestamp(Long.parseLong(entry.getValue())) - 1;
                batchPush(CacheConstant.GROUP_CHAT_HISTORY + key, groupLastTimeStamp, groupOncePushMsgNum);
            }

        });

    }

    private void batchPush(String key, Long score, Long count) {


        while (true) {
            int offset = 0;

            Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet()
                    .rangeByScoreWithScores(key, score, -1, offset, count);
            // 没有消息
            if (CollUtil.isEmpty(typedTuples)) {
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
                    .bizType(PushBizType.batch_his_push)
                    .contentType(ContentType.BATCH)
                    .content(JSON.toJSONString(msgStr))
                    .offlineMsgEvent(false)
                    .qos(1)
                    .build();
            socketManager.send(messageWrap);

        }


    }

    @Override
    public List<String> bind() {
        return Collections.singletonList(ChatTypeEnum.NOTIFY + "-" + EventTypeConst.USER_ONLINE);
    }
}
