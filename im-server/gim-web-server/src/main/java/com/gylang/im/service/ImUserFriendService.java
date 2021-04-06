package com.gylang.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gylang.im.api.dto.UserFriendVO;
import com.gylang.im.entity.ImUserFriend;

import java.util.List;

/**
 * 好友关系表(ImUserFriend)表服务接口
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
public interface ImUserFriendService extends IService<ImUserFriend> {


    boolean checkIsYouFriend(String account, String targetId);

    /**
     * 查询好友
     */
    List<UserFriendVO> selectFriendListByUid(Long id);
}