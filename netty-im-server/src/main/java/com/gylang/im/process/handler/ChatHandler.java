package com.gylang.im.process.handler;

import com.gylang.netty.sdk.IMContext;
import com.gylang.netty.sdk.MessageProvider;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler("chat")
@Component
@Slf4j
public class ChatHandler implements IMRequestHandler {


    @Autowired
    private IMContext context;

    @Override
    public void process(IMSession me, MessageWrap message) {

        MessageProvider messageProvider = context.messageProvider();

        MessageWrap messageWrap = new MessageWrap();
        messageWrap.setSender(me.getAccount());
        messageWrap.setContent(message.getContent());
        messageProvider.sendGroup(me, "default", messageWrap);
    }
}
