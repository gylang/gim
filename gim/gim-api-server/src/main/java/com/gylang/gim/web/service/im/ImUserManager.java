package com.gylang.gim.web.service.im;

import com.gylang.gim.api.domain.entity.UserCache;
import com.gylang.gim.api.dto.response.LoginResponse;

/**
 * @author gylang
 * data 2021/5/16
 */
public interface ImUserManager {

    /**
     * 新增im用户
     *
     * @param userCache 用户信息
     */
    void addUser(UserCache userCache);

    /**
     * im用户接入授权
     *
     * @param loginResponse 用户信息
     */
    void imUserAuth(LoginResponse loginResponse);
}
