//package com.gylang.gim.im.service.impl;
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.gylang.cache.CacheManager;
//import com.gylang.im.common.constant.CacheConstant;
//import com.gylang.im.common.dto.PageDTO;
//import com.gylang.im.dao.entity.HistoryGroupChat;
//import com.gylang.im.dao.entity.HistoryPrivateChat;
//import com.gylang.im.service.HistoryMessageService;
//import com.gylang.im.web.service.HistoryGroupChatService;
//import com.gylang.im.web.service.HistoryPrivateChatService;
//import com.gylang.netty.sdk.util.MsgIdUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
///**
// * @author gylang
// * data 2021/3/21
// */
//@Service
//@Slf4j
//public class HistoryMessageServiceImpl implements HistoryMessageService {
//
//    @Autowired
//    private CacheManager cacheManager;
//
//
//    @Value("${gylang.netty.privateMsgSlot:3}")
//    private Integer slot;
//
//    @Value("${gylang.netty.serverIndex:0}")
//    private Integer serverIndex;
//
//    @Override
//    public void updatePrivateLastMsgId(Long uid, String msgId) {
//
//        // 用户量大 可以使用hash 先分组 再记录
//        long targetSlot = uid & (slot - 1);
//        cacheManager.setMapField(CacheConstant.PRIVATE_LAST_MSG_ID + targetSlot, String.valueOf(uid), msgId);
//
//
//    }
//
//    @Override
//    public void updateGroupLastMsgIdHandler(Long groupId, Long uid, String msgId) {
//
//        cacheManager.setMapField(CacheConstant.GROUP_LAST_MSG_ID + groupId, String.valueOf(uid), msgId);
//
//    }
//
//    @Override
//    public PageDTO<HistoryPrivateChat> privateHistory(PageDTO<HistoryPrivateChat> page, Long uid) {
//
//        long targetSlot = uid & (slot - 1);
//        long lastId = cacheManager.mapGet(CacheConstant.PRIVATE_LAST_MSG_ID + targetSlot, String.valueOf(uid));
//        historyPrivateChatService.page(page, Wrappers.<HistoryPrivateChat>lambdaQuery()
//                .ge(true, HistoryPrivateChat::getId, lastId * 100));
//        return page;
//    }
//
//    @Override
//    public PageDTO<HistoryGroupChat> groupHistory(PageDTO<HistoryGroupChat> page, Long uid) {
//
//        long lastId = cacheManager.mapGet(CacheConstant.GROUP_LAST_MSG_ID + page.getParam().getTargetId(), String.valueOf(uid));
//        historyGroupChatService.page(page, Wrappers.<HistoryGroupChat>lambdaQuery()
//                .ge(true, HistoryGroupChat::getId, lastId * 100));
//        return page;
//    }
//
//    @Override
//    public Long privateHistoryId(String msgId) {
//        return MsgIdUtil.resolveUUID(msgId) * 100 + serverIndex;
//    }
//
//    @Override
//    public Long groupHistoryId(String msgId) {
//        return null;
//    }
//}
