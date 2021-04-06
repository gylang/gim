package com.gylang.gim.im.remote;

import com.gylang.netty.sdk.constant.system.SystemRemoteType;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.spring.netty.annotation.SpringNettyHandler;

/**
 * @author gylang
 * data 2021/4/6
 */

@SpringNettyHandler(SystemRemoteType.P2P_PUSH)
public class RemoteP2GPHandler implements IMRequestHandler {
    @Override
    public Object process(IMSession me, MessageWrap message) {
        return null;
    }
}
