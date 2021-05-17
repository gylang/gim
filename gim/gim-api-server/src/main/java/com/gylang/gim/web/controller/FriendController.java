package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.api.dto.UserFriendVO;
import com.gylang.gim.web.common.mybatis.UserHelper;
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

    /**
     * 获取好友信息
     *
     * @return
     */
    @RequestMapping("getList")
    public CommonResult<List<UserFriendVO>> friendList() {

        return CommonResult.ok(friendService.getFriendList(userHelper.getUid()));
    }

    /**
     * 直接添加好友
     *
     * @param friend
     * @return
     */
    @RequestMapping("add")
    public CommonResult<Boolean> addFriend(@RequestBody ImUserFriendDTO friend) {

        return friendService.addFriend(friend);
    }

    /**
     * 删除好友
     *
     * @param friend
     * @return
     */
    @RequestMapping("del")
    public CommonResult<Boolean> del(@RequestBody ImUserFriendDTO friend) {
        String uid = userHelper.getUid();
        friend.setUid(uid);
        return friendService.del(friend);
    }

    /**
     * 好友申请
     *
     * @param userApply
     * @return
     */
    @RequestMapping("apply")
    public CommonResult<Boolean> applyFriend(@RequestBody UserApply userApply) {
        String uid = userHelper.getUid();
        userApply.setApplyId(uid);
        return friendService.applyFriend(userApply);
    }

    /**
     * 回复好友申请
     *
     * @param userApply
     * @return
     */
    @RequestMapping("answer")
    public CommonResult<Boolean> answer(@RequestBody UserApply userApply) {
        String uid = userHelper.getUid();
        userApply.setAnswerId(uid);
        return friendService.answer(userApply);
    }

}
