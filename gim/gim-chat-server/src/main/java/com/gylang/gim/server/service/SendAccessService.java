package com.gylang.gim.server.service;

import com.gylang.gim.api.domain.manager.BlackWhiteList;

/**
 * 消息发送可进入检查
 *
 * @author gylang
 * data 2021/3/27
 */
public interface SendAccessService {

    /**
     * 单聊可访问性检测
     *
     * @param senderId  发送者id
     * @param receiveId 接收者id
     * @return true 可以发送
     */
    Boolean privateAccessCheck(String senderId, String receiveId);

    /**
     * 群校验可访问性检测
     *
     * @param senderId  发送者id
     * @param receiveId 接收者id
     * @return true 可以发送
     */
    Boolean groupAccessCheck(String senderId, String receiveId);

    /**
     * 查询用户黑白名单信息
     *
     * @param blackWhiteList
     * @return
     */
    BlackWhiteList queryPrivateInfo(BlackWhiteList blackWhiteList);

    /**
     * 更用户组黑白名单
     *
     * @param blackWhiteList
     */
    void updatePrivate(BlackWhiteList blackWhiteList);

    /**
     * 查询群禁言黑白名单信息
     *
     * @param blackWhiteList
     * @return
     */
    BlackWhiteList queryGroupInfo(BlackWhiteList blackWhiteList);


    void updateGroup(BlackWhiteList blackWhiteList);
}
