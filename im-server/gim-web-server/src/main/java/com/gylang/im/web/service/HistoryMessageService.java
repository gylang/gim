package com.gylang.im.web.service;

import com.gylang.im.common.dto.PageDTO;
import com.gylang.im.web.dao.entity.HistoryGroupChat;
import com.gylang.im.web.dao.entity.HistoryPrivateChat;

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
    PageDTO<HistoryPrivateChat> privateHistory(PageDTO<HistoryPrivateChat> page, String uid);


    /**
     * 获取群聊离线消息
     *
     * @param page 获取个人离线消息
     * @param uid  用户ID
     * @return 群聊离线消息
     */
    PageDTO<HistoryGroupChat> groupHistory(PageDTO<HistoryGroupChat> page, String uid);

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
}
