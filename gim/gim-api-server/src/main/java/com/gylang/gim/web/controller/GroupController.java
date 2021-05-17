package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.domain.entity.GroupInfo;
import com.gylang.gim.api.dto.ImGroupCardDTO;
import com.gylang.gim.api.dto.ImUserGroupDTO;
import com.gylang.gim.api.dto.response.UserGroupInfoDTO;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.common.mybatis.UserHelper;
import com.gylang.gim.web.service.ImGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 群组关联
 *
 * @author gylang
 * data 2021/5/16
 */
@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Resource
    private ImGroupService groupService;

    @Resource
    private UserHelper userHelper;

    /**
     * 我的群组
     *
     * @return
     */
    @RequestMapping("/myList")
    public CommonResult<List<ImGroupCardDTO>> myList() {

        return CommonResult.ok(groupService.myList(userHelper.getUid()));
    }

    /**
     * 分页查询群组
     *
     * @param query
     * @return
     */
    @RequestMapping("/page")
    public CommonResult<PageResponse<ImGroupCardDTO>> pageList(@RequestBody Page<ImGroupCardDTO> query) {

        return CommonResult.ok(groupService.pageList(query));
    }

    /**
     * 加入群组
     *
     * @param friend
     * @return
     */
    @RequestMapping("/join")
    public CommonResult<Boolean> join(@RequestBody ImUserGroupDTO friend) {

        return CommonResult.auto(groupService.join(friend));
    }

    /**
     * 退出群组
     *
     * @param friend
     * @return
     */
    @RequestMapping("/exit")
    public CommonResult<Boolean> exit(@RequestBody ImUserGroupDTO friend) {
        String uid = userHelper.getUid();
        friend.setUid(uid);
        return CommonResult.auto(groupService.exit(friend));
    }

    /**
     * 创建群组
     *
     * @param group
     * @return
     */
    @RequestMapping("/create")
    public CommonResult<Boolean> create(@RequestBody ImGroupCardDTO group) {
        String uid = userHelper.getUid();
        group.setCreateBy(uid);
        return CommonResult.auto(groupService.create(group));
    }

    /**
     * 删除群组
     *
     * @param group
     * @return
     */
    @RequestMapping("/del")
    public CommonResult<Boolean> del(@RequestBody ImGroupCardDTO group) {
        String uid = userHelper.getUid();
        group.setCreateBy(uid);
        return CommonResult.auto(groupService.del(group));
    }

    /**
     * 增加群成员
     *
     * @param group
     * @return
     */
    @RequestMapping("/addMembers")
    public CommonResult<Boolean> addMembers(@RequestBody GroupInfo group) {
        return CommonResult.auto(groupService.addMembers(group));
    }

    /**
     * 移除群成员
     *
     * @param group
     * @return
     */
    @RequestMapping("/removeMembers")
    public CommonResult<Boolean> removeMembers(@RequestBody GroupInfo group) {
        return CommonResult.auto(groupService.removeMembers(group));
    }

    /**
     * 群成员信息
     *
     * @param group
     * @return
     */
    @RequestMapping("/membersInfo")
    public CommonResult<PageResponse<UserGroupInfoDTO>> groupDetail(@RequestBody Page<ImUserGroupDTO> group) {
        PageResponse<UserGroupInfoDTO> membersInfo = groupService.membersInfo(group);
        return CommonResult.ok(membersInfo);
    }


}
