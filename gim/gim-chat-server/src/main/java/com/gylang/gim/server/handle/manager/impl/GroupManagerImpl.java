package com.gylang.gim.server.handle.manager.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.mamager.GroupConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.server.handle.manager.ManagerService;
import com.gylang.netty.sdk.domain.model.BaseSessionGroup;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.repo.GIMGroupSessionRepository;
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
            BaseSessionGroup group = JSON.parseObject(content, BaseSessionGroup.class);
            group.setMemberList(null);
            groupSessionRepository.add(group);

        } else if (GroupConstant.DEL.equals(code)) {
            // 删除群组
            BaseSessionGroup group = JSON.parseObject(content, BaseSessionGroup.class);
            group.setMemberList(null);
            groupSessionRepository.add(group);
        } else if (GroupConstant.DEL.equals(code)) {
            // 删除群组
            BaseSessionGroup group = JSON.parseObject(content, BaseSessionGroup.class);
            group.setMemberList(null);
            groupSessionRepository.del(group);
        } else if (GroupConstant.UPDATE.equals(code)) {
            // 修改群组信息
            BaseSessionGroup group = JSON.parseObject(content, BaseSessionGroup.class);
            if (null == groupSessionRepository.findGroupInfo(group.getGroupId())) {
                ReplyMessage.reply(messageWrap, BaseResultCode.INVALID_INPUT.getCode(), "群组不存在");
            }
            group.setMemberList(null);
            groupSessionRepository.add(group);
        } else if (GroupConstant.ADD_MEMBER.equals(code)) {
            // 群组新增用户
            JSONObject body = JSON.parseObject(content);
            String groupId = body.getString("groupId");
            List<String> ids = body.getObject("ids", new TypeReference<List<String>>() {
            });
            groupSessionRepository.addMember(groupId, ids);

        } else if (GroupConstant.REMOVE_MEMBER.equals(code)) {
            // 群组删除成员
            JSONObject body = JSON.parseObject(content);
            String groupId = body.getString("groupId");
            List<String> ids = body.getObject("ids", new TypeReference<List<String>>() {
            });
            groupSessionRepository.removeMember(groupId, ids);
        }


        return null;
    }

    @Override
    public String managerType() {
        return ManagerCmd.GROUP_MANAGER;
    }
}
