package com.gylang.gim.im.biz;

import com.gylang.cache.CacheManager;
import com.gylang.gim.im.constant.BizChatCmd;
import com.gylang.gim.im.domain.AckMessageWrap;
import com.gylang.gim.im.service.SendAccessService;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/5
 */
@Component
@NettyHandler(BizChatCmd.PRIVATE_CHAT)
public class PrivateChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private SendAccessService sendAccessService;

    @Override
    public Object process(IMSession me, MessageWrap message) {


        boolean access = sendAccessService.privateAccessCheck(me.getAccount(), message.getReceive());

        // 发送消息
        if (access) {
            messageProvider.sendMsg(me, message.getReceive(), message.copyBasic());
        }

        return new AckMessageWrap(message);
    }
}
