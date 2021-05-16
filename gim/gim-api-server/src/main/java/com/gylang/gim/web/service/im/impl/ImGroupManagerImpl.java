package com.gylang.gim.web.service.im.impl;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.GimPropertyConstant;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.mamager.GroupConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.entity.GroupInfo;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.web.service.im.ImGroupManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/5/16
 */
@Service
@ConditionalOnProperty(name = GimPropertyConstant.RUN_MODEL_KEY, havingValue = GimPropertyConstant.RUN_MODEL.STANDARD)
public class ImGroupManagerImpl implements ImGroupManager {

    @Resource
    private SocketManager socketManager;

    @Override
    public void create(GroupInfo group) {
        socketManager.send(MessageWrap.builder()
                .qos(1)
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.GROUP_MANAGER)
                .code(GroupConstant.CREATE)
                .content(JSON.toJSONString(group))
                .build());
    }

    @Override
    public void del(GroupInfo group) {
        socketManager.send(MessageWrap.builder()
                .qos(1)
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.GROUP_MANAGER)
                .code(GroupConstant.DEL)
                .content(JSON.toJSONString(group))
                .build());
    }

    @Override
    public void update(GroupInfo group) {
        socketManager.send(MessageWrap.builder()
                .qos(1)
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.GROUP_MANAGER)
                .code(GroupConstant.UPDATE)
                .content(JSON.toJSONString(group))
                .build());
    }

    @Override
    public void addMembers(GroupInfo group) {
        socketManager.send(MessageWrap.builder()
                .qos(1)
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.GROUP_MANAGER)
                .code(GroupConstant.ADD_MEMBER)
                .content(JSON.toJSONString(group))
                .build());
    }

    @Override
    public void removeMembers(GroupInfo group) {
        socketManager.send(MessageWrap.builder()
                .qos(1)
                .type(ChatType.MANAGER)
                .cmd(ManagerCmd.GROUP_MANAGER)
                .code(GroupConstant.REMOVE_MEMBER)
                .content(JSON.toJSONString(group))
                .build());
    }
}
