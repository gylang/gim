package com.gylang.gim.web.service.im;

import com.gylang.gim.api.domain.entity.GroupInfo;

import java.util.List;

/**
 * @author gylang
 * data 2021/5/16
 */
public interface ImGroupManager {

    /**
     * 创建群组
     *
     * @param group 群组信息
     */
    void create(GroupInfo group);

    /**
     * 删除群组
     *
     * @param group 群组信息
     */
    void del(GroupInfo group);

    /**
     * 修改群组
     *
     * @param group 群组信息
     */
    void update(GroupInfo group);

    /**
     * 新增群组用户
     *
     * @param group 群组用户
     */
    void addMembers(GroupInfo group);

    /**
     * 删除群组用户
     *
     * @param group 群组用户
     */
    void removeMembers(GroupInfo group);


}
