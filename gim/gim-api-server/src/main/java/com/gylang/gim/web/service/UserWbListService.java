package com.gylang.gim.web.service;

import com.gylang.gim.api.domain.manager.BlackWhiteList;
import com.gylang.gim.api.dto.response.WBListUserInfoDTO;

/**
 * @author gylang
 * data 2021/5/16
 */
public interface UserWbListService {
    /**
     * 查询用户黑白名单信息
     *
     * @param groupInfo 群id
     * @return 用户黑白名单
     */
    BlackWhiteList whiteBlackList(BlackWhiteList groupInfo);

    /**
     * 更新用户黑白名单信息
     *
     * @param blackWhiteList 黑白名单
     * @return 用户黑白名单
     */
    Boolean update(BlackWhiteList blackWhiteList);

    /**
     * 查询用户黑白名单信息用户信息
     *
     * @param group 群id
     * @return 用户黑白名单用户
     */
    WBListUserInfoDTO membersInfo(BlackWhiteList group);
}
