package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gylang.cache.CacheManager;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.util.MsgIdUtil;
import com.gylang.gim.web.common.constant.CacheConstant;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.entity.HistoryGroupChat;
import com.gylang.gim.web.entity.HistoryPrivateChat;
import com.gylang.gim.web.service.HistoryGroupChatService;
import com.gylang.gim.web.service.HistoryMessageService;
import com.gylang.gim.web.service.HistoryPrivateChatService;
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
        cacheManager.setMapField(CacheConstant.PRIVATE_LAST_MSG_ID + targetSlot, uid, msgId);


    }

    @Override
    public void updateGroupLastMsgIdHandler(String groupId, String uid, String msgId) {

        cacheManager.setMapField(CacheConstant.GROUP_LAST_MSG_ID + groupId, String.valueOf(uid), msgId);

    }

    @Override
    public PageResponse<HistoryPrivateChat> privateHistory(Page<HistoryPrivateChat> page, String uid) {

        long targetSlot = Long.parseLong(uid) & (slot - 1);
        long lastId = cacheManager.mapGet(CacheConstant.PRIVATE_LAST_MSG_ID + targetSlot, uid);
        historyPrivateChatService.page(page, Wrappers.<HistoryPrivateChat>lambdaQuery()
                .ge(true, HistoryPrivateChat::getId, lastId * 100));
        return page.toDTO();
    }

    @Override
    public PageResponse<HistoryGroupChat> groupHistory(Page<HistoryGroupChat> page, String uid) {

        long lastId = cacheManager.mapGet(CacheConstant.GROUP_LAST_MSG_ID + page.getParam().getReceive(), String.valueOf(uid));
        historyGroupChatService.page(page, Wrappers.<HistoryGroupChat>lambdaQuery()
                .ge(true, HistoryGroupChat::getId, lastId * 100));
        return page.toDTO();
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
