package com.gylang.gim.client.api;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.api.dto.UserFriendVO;
import com.gylang.gim.api.dto.request.UserApplyRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

/**
 * @author gylang
 * data 2021/4/6
 */
public interface FriendApi {
    String FRIEND_BASE = "/api/friend";

    /**
     * 获取好友列表
     *
     * @return 好友列表
     */
    @POST(FRIEND_BASE + "/getList")
    Call<CommonResult<List<UserFriendVO>>> friendList();

    /**
     * 添加好友
     *
     * @param friend 好友信息
     * @return 添加好友
     */
    @POST(FRIEND_BASE + "/add")
    Call<CommonResult<Boolean>> addFriend(@Body ImUserFriendDTO friend);

    /**
     * 删除好友
     *
     * @param friend 删除好友
     * @return 删除好友
     */
    @POST(FRIEND_BASE + "/del")
    Call<CommonResult<Boolean>> del(@Body ImUserFriendDTO friend);

    /**
     * 好友申请
     *
     * @param userApply 好友申请
     * @return 好友申请
     */
    @POST(FRIEND_BASE + "/apply")
    Call<CommonResult<Boolean>> applyFriend(@Body UserApplyRequest userApply);

    /**
     * 申请回复
     *
     * @param userApply 申请回复
     * @return 申请回复
     */
    @POST(FRIEND_BASE + "/answer")
    Call<CommonResult<Boolean>> answer(@Body UserApplyRequest userApply);
}
