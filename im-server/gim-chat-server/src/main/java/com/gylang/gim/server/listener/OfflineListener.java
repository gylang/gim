package com.gylang.gim.server.listener;

import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.repo.GIMSessionRepository;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 断开操作
 *
 * @author gylang
 * data 2021/4/6
 */
@Component
public class OfflineListener implements MessageEventListener<GIMSession> {

    @Resource
    private GIMSessionRepository sessionRepository;

    @Override
    @MessageEvent(EventTypeConst.OVER_TIME_CLOSE)
    public void onEvent(String key, GIMSession session) {

        sessionRepository.updateStatus(key, GIMSession.OFFLINE);
        LocalSessionHolderUtil.remove(key);

    }
}
