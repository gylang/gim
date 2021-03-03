package com.gylang.im.process.handler;

import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler("chat")
@Component
@Slf4j
public class ChatHandler implements IMRequestHandler {


    @Resource
    private MessageProvider messageProvider;

    @Override
    public Object process(IMSession me, MessageWrap message) {


        MessageWrap messageWrap = new MessageWrap();
        messageWrap.setSender(me.getAccount());
        messageWrap.setContent("ACK");
        messageWrap.setQos(true);
        messageProvider.sendMsg(me, me, messageWrap);
        return message;
    }
}
