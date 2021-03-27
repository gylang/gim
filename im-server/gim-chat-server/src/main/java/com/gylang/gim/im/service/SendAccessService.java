package com.gylang.gim.im.service;

/**
 * 消息发送可进入检查
 *
 * @author gylang
 * data 2021/3/27
 */
public interface SendAccessService {

    /**
     * 单聊可访问性检测
     * @param account
     * @param receive
     * @return
     */
    boolean privateAccessCheck(String account, String receive);
}
