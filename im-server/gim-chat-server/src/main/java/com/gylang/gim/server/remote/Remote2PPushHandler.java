package com.gylang.gim.server.remote;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.domain.push.PushMessage;
import com.gylang.netty.sdk.constant.system.SystemRemoteType;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.util.MsgIdUtil;
import com.gylang.spring.netty.annotation.SpringNettyHandler;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2021/4/6
 */

@SpringNettyHandler(SystemRemoteType.P2P_PUSH)
public class Remote2PPushHandler implements IMRequestHandler {


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

        return null;
    }

    private void push(IMSession me, MessageWrap message, PushMessage push, List<String> receiveIdList, String msgId) {
        for (String id : receiveIdList) {
            MessageWrap messageWrap = message.copyBasic();
            messageWrap.setMsgId(msgId);
            messageWrap.setContent(push.getContent());
            message.setReceive(id);
            messageProvider.sendMsg(me, id, messageWrap);
        }
    }
}
