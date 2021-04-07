package com.gylang.gim.web.server.service;

/**
 * 消息发送可进入检查
 *
 * @author gylang
 * data 2021/3/27
 */
public interface SendAccessService {

    /**
     * 单聊可访问性检测
     * @param senderId 发送者id
     * @param receiveId 接收者id
     * @return true 可以发送
     */
    Boolean privateAccessCheck(String senderId, String receiveId);
}
