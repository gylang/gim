package com.gylang.gim.im.service;


import com.gylang.netty.sdk.domain.MessageWrap;

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
     * 更新最新接收到的个人消息id
     *
     * @param uid   用户ID
     * @param message 消息
     */
    void storePrivateChat(String uid, MessageWrap message);

    /**
     * 更新最新接收到的群消息id
     *
     * @param groupId 群组
     * @param groupId     群组ID
     * @param message   消息
     */
    void storeGroupChat(String groupId, MessageWrap message);


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
