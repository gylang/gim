package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.manager.WhiteBlackList;
import com.gylang.gim.api.dto.response.WBListUserInfoDTO;
import com.gylang.gim.web.common.mybatis.UserHelper;
import com.gylang.gim.web.service.UserWbListService;
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
@RequestMapping("/api/userWbList")
public class UserWbListController {
    @Resource
    private UserWbListService userWbListService;

    @Resource
    private UserHelper userHelper;

    @RequestMapping("/whiteBlackList")
    public CommonResult<WhiteBlackList> whiteBlackList(@RequestBody WhiteBlackList groupInfo) {

        return CommonResult.ok(userWbListService.whiteBlackList(groupInfo));
    }


    @RequestMapping("/update")
    public CommonResult<Boolean> update(@RequestBody WhiteBlackList whiteBlackList) {

        return CommonResult.auto(userWbListService.update(whiteBlackList));
    }


    @RequestMapping("/membersInfo")
    public CommonResult<WBListUserInfoDTO> groupDetail(@RequestBody WhiteBlackList group) {
        WBListUserInfoDTO membersInfo = userWbListService.membersInfo(group);
        return CommonResult.ok(membersInfo);
    }
}
