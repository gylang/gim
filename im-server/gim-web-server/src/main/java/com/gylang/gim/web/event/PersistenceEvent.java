package com.gylang.gim.web.event;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.admin.event.MessageEventListener;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.web.service.HistoryMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PersistenceEvent implements MessageEventListener<MessageWrap> {


    @Autowired
    private HistoryMessageService historyMessageService;


    @Override
    public void onEvent(String key, MessageWrap m) {

        // 当前直接通过异步线程直接入库 优化可以通过 异步队列,定时扫描,批量入库 缺点是时效性 和 宕机消息丢失的可能性就越高
        MessageWrap msg = JSON.parseObject(m.getContent(), MessageWrap.class);
        // 消息入库
        CompletableFuture.runAsync(() -> historyMessageService.store(msg));
    }


    @Override
    public List<String> bind() {
        return Collections.singletonList(ChatTypeEnum.NOTIFY_CHAT + "-" + EventTypeConst.PERSISTENCE_MSG_EVENT);
    }
}
