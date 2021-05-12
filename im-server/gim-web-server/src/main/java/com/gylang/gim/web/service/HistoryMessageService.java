package com.gylang.gim.web.service;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.entity.HistoryGroupChat;
import com.gylang.gim.web.entity.HistoryPrivateChat;

/**
 * @author gylang
 * data 2021/3/21
 */
public interface HistoryMessageService {

    /**
     * 更新最新接收到的个人消息id
     *
     * @param uid   用户ID
     * @param msgId 消息id
     */
    void updatePrivateLastMsgId(String uid, String msgId);

    /**
     * 更新最新接收到的群消息id
     *
     * @param groupId 群组
     * @param uid     用户ID
     * @param msgId   消息id
     */
    void updateGroupLastMsgIdHandler(String groupId, String uid, String msgId);

    /**
     * 获取个人离线消息
     *
     * @param page 获取个人离线消息
     * @param uid  用户ID
     * @return 个人离线消息
     */
    PageResponse<HistoryPrivateChat> privateHistory(Page<HistoryPrivateChat> page, String uid);


    /**
     * 获取群聊离线消息
     *
     * @param page 获取个人离线消息
     * @param uid  用户ID
     * @return 群聊离线消息
     */
    PageResponse<HistoryGroupChat> groupHistory(Page<HistoryGroupChat> page, String uid);

    /**
     * 获取个人离线消息自增主键id
     *
     * @param msgId 消息id
     * @return 个人离线消息主键id
     */
    Long privateHistoryId(String msgId);

    /**
     * 获取群离线消息自增主键id
     *
     * @param msgId 消息id
     * @return 群离线消息主键id
     */
    Long groupHistoryId(String msgId);

    /**
     * 离线消息入库 和 缓存
     *
     * @param msg
     */
    void store(MessageWrap msg);

    /**
     * 推送离线消息
     * @param key
     */
    void pushHistory(String key);
}
