package com.gylang.netty.sdk.api.repo;

import com.gylang.netty.sdk.api.domain.model.GIMSession;

/**
 * 填充用户信息
 *
 * @author gylang
 * data 2020/11/15
 * @version v0.0.1
 */
public interface NettyUserInfoFillHandler {

    /**
     * 填充用户上下文信息
     *
     * @param session 用户信息
     */
    void fill(GIMSession session);
}
