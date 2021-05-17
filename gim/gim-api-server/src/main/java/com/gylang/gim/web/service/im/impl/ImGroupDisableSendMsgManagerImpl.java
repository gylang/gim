package com.gylang.gim.web.service.im.impl;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.GimPropertyConstant;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.qos.QosConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.manager.BlackWhiteList;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.web.service.im.ImGroupDisableSendMsgManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author gylang
 * data 2021/5/16
 */
@Service
@ConditionalOnProperty(name = GimPropertyConstant.RUN_MODEL_KEY, havingValue = GimPropertyConstant.RUN_MODEL.STANDARD)
public class ImGroupDisableSendMsgManagerImpl implements ImGroupDisableSendMsgManager {
    @Resource
    private SocketManager socketManager;

    @Override
    public void save(BlackWhiteList blackWhiteList) {
        socketManager.send(MessageWrap.builder()
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.GROUP_DISABLE_SEND_MSG_MANAGER)
                .qos(QosConstant.INSURE_ONE_ARRIVE)
                .content(JSON.toJSONString(blackWhiteList))
                .build());
    }

    @Override
    public BlackWhiteList query(BlackWhiteList blackWhiteList) {
        MessageWrap msg = MessageWrap.builder()
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.GROUP_DISABLE_SEND_MSG_MANAGER)
                .qos(QosConstant.INSURE_ONE_ARRIVE)
                .content(JSON.toJSONString(blackWhiteList))
                .build();
        // 同步等待结果
        AtomicReference<BlackWhiteList> result = new AtomicReference<>();
        socketManager.sendAndWaitCallBack(msg, wb -> result.set(JSON.parseObject(wb.getContent(), BlackWhiteList.class)), 5000);
        return result.get();
    }
}
