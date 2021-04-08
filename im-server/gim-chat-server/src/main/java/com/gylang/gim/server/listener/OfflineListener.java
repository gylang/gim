package com.gylang.gim.server.listener;

import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2021/4/6
 */
@Component
public class OfflineListener implements MessageEventListener<IMSession> {



    @Override
    @MessageEvent(EventTypeConst.OVER_TIME_CLOSE)
    public void onEvent(String key, IMSession session) {



    }
}
