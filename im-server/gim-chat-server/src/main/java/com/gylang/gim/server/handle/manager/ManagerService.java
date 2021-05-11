package com.gylang.gim.server.handle.manager;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.GIMSession;

/**
 * @author gylang
 * data 2021/5/6
 */
public interface ManagerService {

    /**
     * 服务调用
     *
     * @param session
     * @param messageWrap
     * @return
     */
    MessageWrap doInvoke(GIMSession session, MessageWrap messageWrap);

    /**
     * 服务类型
     *
     * @return
     */
    String managerType();
}
