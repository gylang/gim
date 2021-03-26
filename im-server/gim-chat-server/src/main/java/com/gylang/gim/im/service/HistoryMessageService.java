package com.gylang.gim.im.service;


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
    void updatePrivateLastMsgId(Long uid, String msgId);

    /**
     * 更新最新接收到的群消息id
     *
     * @param groupId 群组
     * @param uid     用户ID
     * @param msgId   消息id
     */
    void updateGroupLastMsgIdHandler(Long groupId, Long uid, String msgId);



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
