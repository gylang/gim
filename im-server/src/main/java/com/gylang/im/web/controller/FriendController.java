package com.gylang.im.web.controller;

import com.gylang.im.web.dto.ImUserFriendDTO;
import com.gylang.im.web.service.biz.BizFriendService;
import com.gylang.im.common.dto.CommonResult;
import com.gylang.im.common.mybatis.UserHelper;
import com.gylang.im.dao.entity.UserApply;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gylang
 * data 2021/3/6
 */
@RestController
@RequestMapping("friend")
public class FriendController {

    @Resource
    private BizFriendService friendService;

    @Resource
    private UserHelper userHelper;

    @RequestMapping("getList")
    public CommonResult<List<ImUserFriendDTO>> friendList() {

        return CommonResult.ok(friendService.getFriendList(userHelper.getUid()));
    }

    @RequestMapping("add")
    public CommonResult<Boolean> addFriend(@RequestBody ImUserFriendDTO friend) {
        return friendService.addFriend(friend);
    }

    @RequestMapping("del")
    public CommonResult<Boolean> del(@RequestBody ImUserFriendDTO friend) {
        return friendService.del(friend);
    }

    @RequestMapping("apply")
    public CommonResult<Boolean> applyFriend(@RequestBody UserApply userApply) {
        return friendService.applyFriend(userApply);
    }

    @RequestMapping("answer")
    public CommonResult<Boolean> answer(@RequestBody UserApply userApply) {
        return friendService.answer(userApply);
    }

}
