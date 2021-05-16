package com.gylang.gim.web.service;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.domain.entity.GroupInfo;
import com.gylang.gim.api.dto.ImGroupCardDTO;
import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.api.dto.ImUserGroupDTO;
import com.gylang.gim.api.dto.PtUserDTO;
import com.gylang.gim.api.dto.response.UserGroupInfoDTO;
import com.gylang.gim.web.common.mybatis.Page;

import java.util.List;

/**
 * 群组管理
 *
 * @author gylang
 * data 2021/5/16
 */
public interface ImGroupService {

    /**
     * 我的群聊
     *
     * @param uid 用户id
     * @return 我的群聊
     */
    List<ImGroupCardDTO> myList(String uid);

    /**
     * 群组分页
     *
     * @param query 查询条件
     * @return 群组
     */
    PageResponse<ImGroupCardDTO> pageList(Page<ImGroupCardDTO> query);

    /**
     * 加入群组
     *
     * @param group 群组
     * @return 加入结果
     */
    Boolean join(ImUserGroupDTO group);

    /**
     * 退出群聊
     *
     * @param group 群组
     * @return 结果
     */
    Boolean exit(ImUserGroupDTO group);

    /**
     * 创建群组
     *
     * @param group 群组
     * @return 结果
     */
    Boolean create(ImGroupCardDTO group);

    /**
     * 删除群组
     *
     * @param group 群组
     * @return 结果
     */
    Boolean del(ImGroupCardDTO group);

    /**
     * 新增成员
     *
     * @param group 新增成员
     * @return 结果
     */
    Boolean addMembers(GroupInfo group);

    /**
     * 删除成员
     *
     * @param group 删除成员
     * @return 结构
     */
    Boolean removeMembers(GroupInfo group);

    /**
     * 查询用户信息
     *
     * @param group 组
     * @return 用户信息列表
     */
    PageResponse<UserGroupInfoDTO> membersInfo(Page<ImUserGroupDTO> group);
}
