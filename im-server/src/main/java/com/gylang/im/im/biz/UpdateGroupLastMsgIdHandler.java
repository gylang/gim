package com.gylang.im.im.biz;

import com.gylang.cache.CacheManager;
import com.gylang.im.common.constant.CacheConstant;
import com.gylang.im.im.constant.BizChatCmd;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新个人群组中的最新消息msgId
 * @author gylang
 * data 2021/3/18
 */
@Component
@NettyHandler(BizChatCmd.GROUP_CHAT_LAST_MSG_ID)
public class UpdateGroupLastMsgIdHandler implements IMRequestHandler {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        cacheManager.setMapField(CacheConstant.PRIVATE_LAST_MSG_ID + message.getReceive(), String.valueOf(me.getAccount()), message.getContent());
        return null;
    }
}
