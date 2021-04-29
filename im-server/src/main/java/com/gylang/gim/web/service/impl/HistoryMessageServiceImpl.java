package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gylang.cache.CacheManager;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.web.common.constant.CacheConstant;
import com.gylang.gim.web.dao.entity.HistoryGroupChat;
import com.gylang.gim.web.dao.entity.HistoryPrivateChat;
import com.gylang.gim.web.service.HistoryGroupChatService;
import com.gylang.gim.web.service.HistoryMessageService;
import com.gylang.gim.web.service.HistoryPrivateChatService;
import com.gylang.netty.sdk.util.MsgIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author gylang
 * data 2021/3/21
 */
@Service
@Slf4j
public class HistoryMessageServiceImpl implements HistoryMessageService {

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

    @Override
    public void updatePrivateLastMsgId(String uid, String msgId) {

        // 用户量大 可以使用hash 先分组 再记录
        long targetSlot = Long.parseLong(uid) & (slot - 1);
        cacheManager.setMapField(CacheConstant.PRIVATE_LAST_MSG_ID + targetSlot, String.valueOf(uid), msgId);


    }

    @Override
    public void updateGroupLastMsgIdHandler(String groupId, String uid, String msgId) {

        cacheManager.setMapField(CacheConstant.GROUP_LAST_MSG_ID + groupId, String.valueOf(uid), msgId);

    }

    @Override
    public PageResponse<HistoryPrivateChat> privateHistory(PageResponse<HistoryPrivateChat> page, String uid) {

        long targetSlot = Long.parseLong(uid) & (slot - 1);
        long lastId = cacheManager.mapGet(CacheConstant.PRIVATE_LAST_MSG_ID + targetSlot, String.valueOf(uid));
        historyPrivateChatService.page(page, Wrappers.<HistoryPrivateChat>lambdaQuery()
                .ge(true, HistoryPrivateChat::getId, lastId * 100));
        return page;
    }

    @Override
    public PageResponse<HistoryGroupChat> groupHistory(PageResponse<HistoryGroupChat> page, String uid) {

        long lastId = cacheManager.mapGet(CacheConstant.GROUP_LAST_MSG_ID + page.getParam().getReceive(), String.valueOf(uid));
        historyGroupChatService.page(page, Wrappers.<HistoryGroupChat>lambdaQuery()
                .ge(true, HistoryGroupChat::getId, lastId * 100));
        return page;
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
