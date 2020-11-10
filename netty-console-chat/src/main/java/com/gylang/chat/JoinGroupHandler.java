package com.gylang.chat;

import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.repo.GroupRepository;
import lombok.Setter;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
@NettyHandler("register")
public class JoinGroupHandler implements IMRequestHandler {
    @Setter
    private GroupRepository groupRepository;
    @Override
    public void process(IMSession me, MessageWrap message) {
        System.out.println("注册监听" + message.getContent());
        AbstractSessionGroup aDefault = groupRepository.findByKey("default");
        me.setAccount(message.getContent());
        aDefault.join(me);
    }
}
