package com.gylang.im.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gylang.im.dao.entity.ImUserFriend;
/**
 * 好友关系表(ImUserFriend)表服务接口
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
public interface ImUserFriendService extends IService<ImUserFriend> {


    boolean checkIsYouFriend(String account, String targetId);
}