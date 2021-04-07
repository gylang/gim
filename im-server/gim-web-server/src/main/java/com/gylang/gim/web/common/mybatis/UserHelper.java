package com.gylang.gim.web.common.mybatis;

import com.gylang.gim.web.domain.UserCache;

/**
 * @author gylang
 * data 2021/1/5
 */
public interface UserHelper {

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    Long getUid();

    /**
     * 获取用户id
     *
     * @param token token
     * @return 用户ID
     */
    Long getUid(String token);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    UserCache getUserInfo();

    /**
     * 获取用户信息
     *
     * @param token token
     * @return 用户信息
     */
    UserCache getUserInfo(String token);
}
