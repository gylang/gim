package com.gylang.gim.server.handle.remote;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.domain.push.PushMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.util.MsgIdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 组推送
 *
 * @author gylang
 * data 2021/4/6
 */

@Component
@NettyHandler(ChatTypeEnum.P2G_PUSH)
public class Remote2GPushHandler implements IMRequestHandler {

    @Resource
    private ThreadPoolExecutor executor;

    private static final int BIZ_THREAD_CAP = 20;

    @Resource
    private MessageProvider messageProvider;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        PushMessage push = JSON.parseObject(message.getContent(), PushMessage.class);
        List<String> receiveIdList = push.getReceiveId();
        String msgId = MsgIdUtil.increase(message);
        if (receiveIdList.size() < BIZ_THREAD_CAP) {
            push(me, message, push, receiveIdList, msgId);
        } else {
            executor.execute(() -> push(me, message, push, receiveIdList, msgId));
        }

        return ReplyMessage.reply(message, BaseResultCode.NOT_ACCESS_PRIVATE_RESOURCE);
    }

    private void push(IMSession me, MessageWrap message, PushMessage push, List<String> receiveIdList, String msgId) {
        for (String id : receiveIdList) {
            MessageWrap messageWrap = message.copyBasic();
            messageWrap.setMsgId(msgId);
            messageWrap.setContent(push.getContent());
            message.setReceive(id);
            messageProvider.sendGroup(me, id, messageWrap);
        }
    }
}
