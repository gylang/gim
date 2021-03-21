package com.gylang.chat;

import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import lombok.Setter;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
@NettyHandler("register")
public class JoinGroupHandler implements IMRequestHandler {
    @Setter
    private DefaultGroupRepository defaultGroupRepository;

    @Override
    public Object process(IMSession me, MessageWrap message) {
        System.out.println("注册服务" + message.getContent());
        AbstractSessionGroup aDefault = defaultGroupRepository.findByKey("default");
        me.setAccount(Long.parseLong(message.getContent()));
        boolean join = aDefault.join(me);
        if (join) {
            me.getGroupList().add(aDefault);
        }

        return null;
    }
}