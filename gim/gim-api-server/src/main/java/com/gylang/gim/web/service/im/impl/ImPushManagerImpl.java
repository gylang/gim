package com.gylang.gim.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.GimPropertyConstant;
import com.gylang.gim.api.constant.cmd.PushChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.push.PushMessage;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.web.service.im.ImUserPushManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/5/17
 */
@Service
@ConditionalOnProperty(name = GimPropertyConstant.RUN_MODEL_KEY, havingValue = GimPropertyConstant.RUN_MODEL.STANDARD)
public class ImPushManagerImpl implements ImUserPushManager {

    @Resource
    private SocketManager socketManager;

    @Override
    public void push(String send, int type, boolean offlineMsgEvent, String content, int qos, String... receive) {

        PushMessage pushMessage = new PushMessage();
        pushMessage.setQos(qos);
        pushMessage.setSender(send);
        pushMessage.setType(type);
        pushMessage.setOfflineMsgEvent(offlineMsgEvent);
        pushMessage.setReceiveId(CollUtil.newArrayList(receive));
        MessageWrap applyMsg = MessageWrap.builder()
                .qos(qos)
                .cmd(PushChatCmd.P2P_PUSH)
                .offlineMsgEvent(true)
                .content(JSON.toJSONString(pushMessage))
                .type(ChatType.P2P_PUSH)
                .build();
        socketManager.send(applyMsg);
    }

    @Override
    public void push(int qos, PushMessage msg) {

        MessageWrap applyMsg = MessageWrap.builder()
                .qos(qos)
                .cmd(PushChatCmd.P2P_PUSH)
                .offlineMsgEvent(true)
                .content(JSON.toJSONString(msg))
                .type(ChatType.P2P_PUSH)
                .build();
        socketManager.send(applyMsg);
    }


    @Override
    public void push(String content, int qos, boolean offlineMsgEvent) {

        MessageWrap applyMsg = MessageWrap.builder()
                .qos(qos)
                .cmd(PushChatCmd.P2P_PUSH)
                .offlineMsgEvent(true)
                .content(content)
                .type(ChatType.P2P_PUSH)
                .build();
        socketManager.send(applyMsg);
    }
}
