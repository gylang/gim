package com.gylang.netty.sdk.repo;


import com.gylang.netty.sdk.domain.model.GIMSession;

import java.util.Collection;
import java.util.List;

/**
 * 单用户会话仓库
 *
 * @author gylang
 * data 2020/11/30
 */
public interface GIMSessionRepository {

    /**
     * 获取用户会话信息
     * @param userId 用户id
     * @return 用户
     */
    GIMSession findUserId(String userId);

    /**
     * 获取用户会话信息
     * @param userIds
     * @return
     */
    List<GIMSession> findByUserIds(Collection<String> userIds);

    /**
     * 移除用户会话
     * @param userId
     * @return
     */
    Long removeByUserId(String userId);
    /**
     * 移除用户会话
     * @param userIds
     * @return
     */
    Long removeByUserIds(Collection<String> userIds);

    /**
     * 新增用户会话
     * @param session
     */
    void addSession(GIMSession session);
    /**
     * 新增用户会话
     * @param session
     */
    void addSessionList(List<GIMSession> session);
    /**
     * 状态变更
     * @param userId
     * @param status 状态
     */
    void updateStatus(String userId, Integer status);
}
