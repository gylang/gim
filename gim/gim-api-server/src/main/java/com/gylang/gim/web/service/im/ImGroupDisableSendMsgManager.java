package com.gylang.gim.web.service.im;

import com.gylang.gim.api.domain.manager.BlackWhiteList;

/**
 * 群聊禁言管理
 *
 * @author gylang
 * data 2021/5/16
 */
public interface ImGroupDisableSendMsgManager {

    /**
     * 群聊发送消息黑白名单管理
     *
     * @param blackWhiteList 黑白名单
     */
    void save(BlackWhiteList blackWhiteList);

    /**
     * 查询黑白名单
     * @param blackWhiteList 黑白名单
     * @return 黑白名单
     */
    BlackWhiteList query(BlackWhiteList blackWhiteList);
}
