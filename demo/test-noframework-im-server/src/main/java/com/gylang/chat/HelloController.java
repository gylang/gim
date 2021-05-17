package com.gylang.chat;

import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.GimController;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
@NettyHandler(ChatType.PRIVATE_CHAT)
@Slf4j
public class HelloController implements GimController<Void> {



    @Override
    public Void process(GIMSession me, Void requestBody) {

        return null;
    }
}
