package com.gylang.im.web.service.biz;

import com.gylang.im.common.dto.CommonResult;
import com.gylang.im.web.dao.entity.UserApply;
import com.gylang.im.api.dto.ImUserFriendDTO;

import java.util.List;

/**
 * @author gylang
 * data 2021/3/6
 */
public interface BizFriendService {

    /**
     * 获取好友列表
     *
     * @param uid 用户id
     * @return 好友列表
     */
    List<ImUserFriendDTO> getFriendList(Long uid);

    /**
     * 添加好友
     *
     * @param friend 双方id
     * @return 添加结果
     */
    CommonResult<Boolean> addFriend(ImUserFriendDTO friend);

    BizFriendService getProxy();

    List<ImUserFriendDTO> updateCacheList(Long id);

    /**
     * 删除好友
     *
     * @param friend 好友信息
     * @return 删除结果
     */
    CommonResult<Boolean> del(ImUserFriendDTO friend);

    /**
     * 申请好友
     *
     * @param userApply 好友申请
     * @return 申请结果
     */
    CommonResult<Boolean> applyFriend(UserApply userApply);

    /**
     * 申请回复
     *
     * @param userApply 同意/拒绝
     * @return 回复结果
     */
    CommonResult<Boolean> answer(UserApply userApply);
}
