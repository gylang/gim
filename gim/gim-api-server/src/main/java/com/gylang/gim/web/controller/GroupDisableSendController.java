package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.manager.BlackWhiteList;
import com.gylang.gim.api.dto.response.WBListUserInfoDTO;
import com.gylang.gim.web.common.mybatis.UserHelper;
import com.gylang.gim.web.service.GroupDisableSendService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 群组禁言管理
 *
 * @author gylang
 * data 2021/5/16
 */
@RestController
@RequestMapping("/api/groupDisableSend")
public class GroupDisableSendController {
    @Resource
    private GroupDisableSendService groupDisableSendService;

    @Resource
    private UserHelper userHelper;

    /**
     * 禁言白名单
     *
     * @param groupInfo
     * @return
     */
    @RequestMapping("/whiteBlackList")
    public CommonResult<BlackWhiteList> whiteBlackList(@RequestBody BlackWhiteList groupInfo) {

        return CommonResult.ok(groupDisableSendService.whiteBlackList(groupInfo));
    }


    /**
     * 更新禁言关联
     *
     * @param blackWhiteList
     * @return
     */
    @RequestMapping("/update")
    public CommonResult<Boolean> update(@RequestBody BlackWhiteList blackWhiteList) {

        return CommonResult.auto(groupDisableSendService.update(blackWhiteList));
    }

    /**
     * 获取禁言管理用户信息
     *
     * @param group
     * @return
     */
    @RequestMapping("/membersInfo")
    public CommonResult<WBListUserInfoDTO> groupDetail(@RequestBody BlackWhiteList group) {
        WBListUserInfoDTO membersInfo = groupDisableSendService.membersInfo(group);
        return CommonResult.ok(membersInfo);
    }
}
