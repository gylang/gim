package com.gylang.chat;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import lombok.Setter;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
@NettyHandler(ChatTypeEnum.CLIENT_AUTH)
public class JoinGroupHandler implements IMRequestHandler {
    @Setter
    private DefaultGroupRepository defaultGroupRepository;

    @Override
    public Object process(GIMSession me, MessageWrap message) {
        AbstractSessionGroup aDefault = defaultGroupRepository.findByKey("default");
        me.setAccount(message.getContent());
        boolean join = aDefault.join(me);
        if (join) {
            me.getGroupList().add(aDefault);
        }

        return null;
    }
}
