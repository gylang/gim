package com.gylang.gim.data.event;

import com.gylang.gim.admin.event.AdminMessageEventListener;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.data.service.HistoryMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author gylang
 * data 2021/4/16
 */
@Component
@Slf4j
public class UserLoginEvent implements AdminMessageEventListener<MessageWrap> {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Value("${gim.privateChat.history.oncePushMsgNum:50}")
    private Long priOncePushMsgNum;
    @Value("${gim.groupChat.history.oncePushMsgNum:50}")
    private Long groupOncePushMsgNum;
    @Autowired
    private SocketManager socketManager;
    @Autowired
    private HistoryMessageService historyMessageService;
    @Override
    public void onEvent(String key, MessageWrap m) {

        // 异步任务
        if (log.isDebugEnabled()) {
            log.debug("接收到用户[{}]上线通知, 准备推送离线消息", key);
        }
        CompletableFuture.runAsync(() -> historyMessageService.pushHistory(m.getContent()));

    }


    @Override
    public List<String> bind() {
        return Collections.singletonList(ChatTypeEnum.NOTIFY_CHAT + "-" + EventTypeConst.USER_ONLINE);
    }
}
