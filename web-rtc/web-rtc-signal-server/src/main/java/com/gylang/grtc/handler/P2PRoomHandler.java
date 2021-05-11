package com.gylang.grtc.handler;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.grtc.constant.SignalCmd;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * p2p 信令交换
 *
 * @author gylang
 * data 2021/4/23
 */
@Component
@NettyHandler(SignalCmd.P2P_ROOM_SIGNAL_EXCHANGE)
public class P2PRoomHandler implements IMRequestHandler {

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public Object process(GIMSession me, MessageWrap message) {
        // 直接拿转发信令

        messageProvider.sendMsg(me, message.getReceive(), message);
        return null;
    }
}
