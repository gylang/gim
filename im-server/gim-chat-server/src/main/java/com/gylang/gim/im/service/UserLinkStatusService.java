package com.gylang.gim.im.service;

import com.gylang.im.api.domain.UserLinkStatus;

import java.util.List;

/**
 * @author gylang
 * data 2021/4/6
 */
public interface UserLinkStatusService {


    /**
     * 更新用户状态
     *
     * @param account 用户id
     * @param status  状态实体
     */
    void setStatus(String account, UserLinkStatus status);


    /**
     * 查询用户状态列表
     *
     * @param account 用户id
     * @return
     */
    List<UserLinkStatus> statusList(List<String> account);
}
