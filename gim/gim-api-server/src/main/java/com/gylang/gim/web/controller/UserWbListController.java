package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.manager.BlackWhiteList;
import com.gylang.gim.api.dto.response.WBListUserInfoDTO;
import com.gylang.gim.web.service.UserWbListService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户黑白名单管理
 *
 * @author gylang
 * data 2021/5/16
 */
@RestController
@RequestMapping("/api/userWbList")
public class UserWbListController {
    @Resource
    private UserWbListService userWbListService;


    /**
     * 查询
     *
     * @param blackWhiteList
     * @return
     */
    @RequestMapping("/whiteBlackList")
    public CommonResult<BlackWhiteList> whiteBlackList(@RequestBody BlackWhiteList blackWhiteList) {

        return CommonResult.ok(userWbListService.whiteBlackList(blackWhiteList));
    }

    /**
     * 更新用户黑白名单
     *
     * @param blackWhiteList
     * @return
     */
    @RequestMapping("/update")
    public CommonResult<Boolean> update(@RequestBody BlackWhiteList blackWhiteList) {

        return CommonResult.auto(userWbListService.update(blackWhiteList));
    }

    /**
     * 用户信息
     *
     * @param group
     * @return
     */
    @RequestMapping("/membersInfo")
    public CommonResult<WBListUserInfoDTO> groupDetail(@RequestBody BlackWhiteList group) {
        WBListUserInfoDTO membersInfo = userWbListService.membersInfo(group);
        return CommonResult.ok(membersInfo);
    }
}
