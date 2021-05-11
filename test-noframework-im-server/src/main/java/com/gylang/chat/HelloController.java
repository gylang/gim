package com.gylang.chat;

import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.handler.GimController;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
@NettyHandler("chat")
@Slf4j
public class HelloController implements GimController<Void> {



    @Override
    public Void process(GIMSession me, Void requestBody) {

        return null;
    }
}
