package com.gylang.gim.server.handle.manager.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.mamager.GroupConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.entity.GroupInfo;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.server.handle.manager.ManagerService;
import com.gylang.netty.sdk.api.domain.model.BaseSessionGroup;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gylang
 * data 2021/5/15
 */
@Service
public class GroupManagerImpl implements ManagerService {

    @Resource
    private GIMGroupSessionRepository groupSessionRepository;

    @Override
    public MessageWrap doInvoke(GIMSession session, MessageWrap messageWrap) {

        String content = messageWrap.getContent();
        String code = messageWrap.getCode();
        if (GroupConstant.CREATE.equals(code)) {
            // 新增群组
            GroupInfo group = JSON.parseObject(content, GroupInfo.class);
            BaseSessionGroup sessionGroup = toGroup(group);
            groupSessionRepository.add(sessionGroup);
        } else if (GroupConstant.DEL.equals(code)) {
            // 删除群组
            GroupInfo group = JSON.parseObject(content, GroupInfo.class);
            BaseSessionGroup sessionGroup = toGroup(group);
            groupSessionRepository.del(sessionGroup);
        } else if (GroupConstant.UPDATE.equals(code)) {
            // 修改群组信息
            GroupInfo group = JSON.parseObject(content, GroupInfo.class);
            if (null == groupSessionRepository.findGroupInfo(group.getGroupId())) {
                ReplyMessage.reply(messageWrap, BaseResultCode.INVALID_INPUT.getCode(), "群组不存在");
            }
            BaseSessionGroup sessionGroup = toGroup(group);
            groupSessionRepository.add(sessionGroup);
        } else if (GroupConstant.ADD_MEMBER.equals(code)) {
            // 群组新增用户
            GroupInfo body = JSON.parseObject(content, GroupInfo.class);
            String groupId = body.getGroupId();
            List<String> ids = body.getIds();
            groupSessionRepository.addMember(groupId, ids);

        } else if (GroupConstant.REMOVE_MEMBER.equals(code)) {
            // 群组删除成员
            GroupInfo body = JSON.parseObject(content, GroupInfo.class);
            String groupId = body.getGroupId();
            List<String> ids = body.getIds();
            groupSessionRepository.removeMember(groupId, ids);
        }


        return null;
    }

    private BaseSessionGroup toGroup(GroupInfo group) {
        BaseSessionGroup sessionGroup = new BaseSessionGroup();
        sessionGroup.setGroupId(group.getGroupId());
        sessionGroup.setCreator(group.getCreator());
        sessionGroup.setKey(group.getKey());
        sessionGroup.setPassword(group.getPassword());
        sessionGroup.setMaster(group.getMaster());
        return sessionGroup;
    }

    @Override
    public String managerType() {
        return ManagerCmd.GROUP_MANAGER;
    }
}
