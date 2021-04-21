package com.gylang.gim.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gylang.gim.web.dao.entity.ImUserGroup;

/**
 * 群聊好友人员关系(ImUserGroup)表服务接口
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
public interface ImUserGroupService extends IService<ImUserGroup> {


    boolean checkIsYouGroup(String account, String targetId);
}