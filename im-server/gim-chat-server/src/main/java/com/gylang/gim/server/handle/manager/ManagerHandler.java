package com.gylang.gim.server.handle.manager;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2021/5/6
 */
@NettyHandler(ChatTypeEnum.MANAGER)
@Component
public class ManagerHandler implements IMRequestHandler {

    @Resource
    private ThreadPoolExecutor executor;

    private static final int BIZ_THREAD_CAP = 20;

    @Resource
    private MessageProvider messageProvider;

    @Override
    public Object process(IMSession me, MessageWrap message) {



        return ReplyMessage.reply(message, BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE);
    }


}
