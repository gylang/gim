package com.gylang.gim.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.domain.entity.GroupInfo;
import com.gylang.gim.api.dto.ImGroupCardDTO;
import com.gylang.gim.api.dto.ImUserGroupDTO;
import com.gylang.gim.api.dto.response.UserGroupInfoDTO;
import com.gylang.gim.web.common.mybatis.BaseDO;
import com.gylang.gim.web.common.mybatis.InnerBaseDO;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.common.util.Asserts;
import com.gylang.gim.web.common.util.MappingUtil;
import com.gylang.gim.web.entity.ImGroupCard;
import com.gylang.gim.web.entity.ImUserGroup;
import com.gylang.gim.web.entity.PtUser;
import com.gylang.gim.web.service.ImGroupCardService;
import com.gylang.gim.web.service.ImGroupService;
import com.gylang.gim.web.service.ImUserGroupService;
import com.gylang.gim.web.service.PtUserService;
import com.gylang.gim.web.service.im.ImGroupManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2021/5/16
 */
@Service
public class ImGroupServiceImpl implements ImGroupService {

    @Resource
    private ImGroupCardService groupCardService;
    @Resource
    private ImUserGroupService userGroupService;
    @Resource
    private ImGroupManager imGroupManager;
    @Resource
    private PtUserService userService;

    @Override
    public List<ImGroupCardDTO> myList(String uid) {

        // 所在群组
        List<ImUserGroup> userGroupList = userGroupService.lambdaQuery()
                .eq(ImUserGroup::getUid, uid)
                .list();

        // 查询基本信息
        List<String> ids = CollUtil.map(userGroupList, ImUserGroup::getGroupId, true);
        return MappingUtil.mapAsList(groupCardService.listByIds(ids), ImGroupCardDTO.class);
    }

    @Override
    public PageResponse<ImGroupCardDTO> pageList(Page<ImGroupCardDTO> query) {

        //
        ImGroupCardDTO param = query.getParam();
        Page<ImGroupCard> page = groupCardService.lambdaQuery()
                .likeRight(StrUtil.isNotEmpty(param.getName()), ImGroupCard::getName, param.getName())
                .eq(null != param.getType(), ImGroupCard::getType, param.getType())
                .eq(null != param.getGroupMaster(), ImGroupCard::getGroupMaster, param.getGroupMaster())
                .eq(null != param.getCreateBy(), InnerBaseDO::getCreateBy, param.getCreateBy())
                .eq(BaseDO::getId, param)
                .page(query.converterQuery(ImGroupCard.class));


        return page.toDTO(ImGroupCardDTO.class);
    }

    @Override
    public Boolean join(ImUserGroupDTO group) {

        ImGroupCard groupCard = groupCardService.getById(group.getGroupId());
        Asserts.notNull(groupCard, "群组不存在");

        boolean save = userGroupService.save(MappingUtil.map(group, ImUserGroup.class));
        Asserts.isTrue(save, "新增失败");
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupId(group.getGroupId());
        groupInfo.setIds(CollUtil.newArrayList(group.getUid()));
        // 通知Im服务
        imGroupManager.addMembers(groupInfo);
        return true;
    }

    @Override
    public Boolean exit(ImUserGroupDTO group) {
        ImGroupCard groupCard = groupCardService.getById(group.getGroupId());
        Asserts.notNull(groupCard, "群组不存在");

        boolean remove = userGroupService.lambdaUpdate()
                .eq(ImUserGroup::getGroupId, group.getGroupId())
                .eq(ImUserGroup::getUid, group.getUid())
                .remove();

        Asserts.isTrue(remove, "删除失败");
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupId(group.getGroupId());
        groupInfo.setIds(CollUtil.newArrayList(group.getUid()));
        // 通知Im服务
        imGroupManager.removeMembers(groupInfo);
        return true;
    }

    @Override
    public Boolean create(ImGroupCardDTO group) {

        ImGroupCard entity = MappingUtil.map(group, new ImGroupCard());
        boolean save = groupCardService.save(entity);
        Asserts.isTrue(save, "新增失败");

        GroupInfo groupInfo = cardInfo2GroupInfo(entity);
        imGroupManager.create(groupInfo);
        return save;
    }

    private GroupInfo cardInfo2GroupInfo(ImGroupCard entity) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setCreator(entity.getCreateBy());
        groupInfo.setGroupId(entity.getId().toString());
        groupInfo.setMaster(entity.getGroupMaster().toString());
        groupInfo.setName(entity.getName());
        return groupInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean del(ImGroupCardDTO group) {

        Long groupId = group.getId();
        Asserts.notNull(groupId, "群组不存在");
        groupCardService.removeById(groupId);
        userGroupService.lambdaUpdate()
                .eq(ImUserGroup::getGroupId, groupId);

        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setCreator(group.getCreateBy());
        groupInfo.setGroupId(group.getId().toString());
        groupInfo.setMaster(group.getGroupMaster());
        groupInfo.setName(group.getName());
        imGroupManager.del(groupInfo);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMembers(GroupInfo group) {

        Asserts.notNull(group.getGroupId(), "群组不存在");
        ImGroupCard groupCard = groupCardService.getById(group.getGroupId());
        Asserts.notNull(groupCard, "群组不存在");

        List<ImUserGroup> userGroupList = group.getIds().stream().map(id -> {
            ImUserGroup userGroup = new ImUserGroup();
            userGroup.setGroupId(group.getGroupId());
            userGroup.setUid(id);
            return userGroup;
        }).collect(Collectors.toList());
        return userGroupService.saveBatch(userGroupList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMembers(GroupInfo group) {
        Asserts.notNull(group.getGroupId(), "群组不存在");
        ImGroupCard groupCard = groupCardService.getById(group.getGroupId());
        Asserts.notNull(groupCard, "群组不存在");

        return userGroupService.lambdaUpdate()
                .eq(ImUserGroup::getGroupId, group.getGroupId())
                .in(ImUserGroup::getUid, group.getIds())
                .remove();
    }

    @Override
    public PageResponse<UserGroupInfoDTO> membersInfo(Page<ImUserGroupDTO> group) {

        ImUserGroupDTO param = group.getParam();
        Page<ImUserGroup> userGroupPage = userGroupService.lambdaQuery()
                .eq(ImUserGroup::getGroupId, param.getGroupId())
                .likeRight(ImUserGroup::getNickname, param.getNickname())
                .likeRight(ImUserGroup::getUid, param.getUid())
                .page(group.converterQuery(ImUserGroup.class));

        // 非必要 暂时先不进行联表
        List<ImUserGroup> records = userGroupPage.getRecords();
        // 用户信息
        List<String> uids = records.stream().map(ImUserGroup::getUid).collect(Collectors.toList());
        Map<Long, PtUser> userMap = userService.listByIds(uids).stream().collect(Collectors.toMap(PtUser::getId, u -> u));

        // 拼装结果
        PageResponse<UserGroupInfoDTO> response = userGroupPage.toDTO(UserGroupInfoDTO.class);
        for (UserGroupInfoDTO record : response.getRecords()) {
            PtUser user = userMap.get(Long.parseLong(record.getUid()));
            if (null != user) {
                record.setUsername(user.getUsername());
                record.setEmail(user.getEmail());
                record.setTel(user.getTel());
                record.setNickname(user.getNickname());
                record.setStatus(user.getStatus());
                record.setNickname(ObjectUtil.defaultIfNull(record.getNickname(), user.getNickname()));
            }
        }
        return response;
    }
}
