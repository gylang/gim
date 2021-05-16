package com.gylang.gim.web.service.im;

import com.gylang.gim.api.domain.manager.WhiteBlackList;

/**
 * 用户黑白名单管理
 *
 * @author gylang
 * data 2021/5/16
 */
public interface ImUserWhiteBlackManager {

    /**
     * 黑白名单管理
     * @param whiteBlackList 黑白名单
     */
    void save(WhiteBlackList whiteBlackList);

    /**
     * 查询黑白名单
     * @param whiteBlackList 黑白名单
     * @return 黑白名单
     */
    WhiteBlackList query(WhiteBlackList whiteBlackList);
}
