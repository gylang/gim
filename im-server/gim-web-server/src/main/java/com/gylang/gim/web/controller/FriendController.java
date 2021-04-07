package com.gylang.gim.web.controller;

import com.gylang.gim.web.api.domain.common.CommonResult;
import com.gylang.gim.web.api.dto.ImUserFriendDTO;
import com.gylang.gim.web.common.mybatis.UserHelper;
import com.gylang.gim.web.api.dto.UserFriendVO;
import com.gylang.gim.web.entity.UserApply;
import com.gylang.gim.web.service.biz.BizFriendService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gylang
 * data 2021/3/6
 */
@RestController
@RequestMapping("/api/friend")
public class FriendController {

    @Resource
    private BizFriendService friendService;

    @Resource
    private UserHelper userHelper;

    @RequestMapping("getList")
    public CommonResult<List<UserFriendVO>> friendList() {

        return CommonResult.ok(friendService.getFriendList(userHelper.getUid()));
    }

    @RequestMapping("add")
    public CommonResult<Boolean> addFriend(@RequestBody ImUserFriendDTO friend) {

        return friendService.addFriend(friend);
    }

    @RequestMapping("del")
    public CommonResult<Boolean> del(@RequestBody ImUserFriendDTO friend) {
        Long uid = userHelper.getUid();
        friend.setUid(uid);
        return friendService.del(friend);
    }

    @RequestMapping("apply")
    public CommonResult<Boolean> applyFriend(@RequestBody UserApply userApply) {
        Long uid = userHelper.getUid();
        userApply.setApplyId(String.valueOf(uid));
        return friendService.applyFriend(userApply);
    }

    @RequestMapping("answer")
    public CommonResult<Boolean> answer(@RequestBody UserApply userApply) {
        Long uid = userHelper.getUid();
        userApply.setAnswerId(String.valueOf(uid));
        return friendService.answer(userApply);
    }

}
