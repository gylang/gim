package com.gylang.netty.sdk.api.repo;


import com.gylang.netty.sdk.api.domain.model.BaseSessionGroup;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 会话组仓库
 *
 * @author gylang
 * data 2020/11/30
 */
public interface GIMGroupSessionRepository {

    /**
     * 新增群组
     *
     * @param group 群组
     */
    void add(BaseSessionGroup group);

    /**
     * 获取群组信息
     *
     * @param groupId 群组id
     * @return 群组信息
     */
    BaseSessionGroup findGroupInfo(String groupId);

    /**
     * 获取群组信息
     *
     * @param groupIds 群组id
     * @return 群组信息
     */
    List<BaseSessionGroup> findGroups(Collection<String> groupIds);

    /**
     * 校验是否为群用户
     *
     * @param groupId  群组id
     * @param memberId 用户id
     * @return 是否为该成员
     */
    Boolean checkIsMember(String groupId, String memberId);

    /**
     * 获取所有的群成员
     *
     * @param groupId 群组id
     * @return 用户id
     */
    Set<String> getAllMemberIds(String groupId);

    /**
     * 获取群成员
     *
     * @param groupId   群组id
     * @param memberIds 用户id
     * @return 用户id
     */
    Set<String> getMemberIds(String groupId, Collection<String> memberIds);

    /**
     * 新增成员
     *
     * @param groupId  群组id
     * @param memberId 用户id
     * @return
     */
    boolean addMember(String groupId, String memberId);

    /**
     * 新增成员
     *
     * @param groupId   群组id
     * @param memberIds 用户id
     * @return
     */
    boolean addMember(String groupId, Collection<String> memberIds);

    /**
     * 删除成员
     *
     * @param groupId  群组id
     * @param memberId 用户id
     */
    void removeMember(String groupId, String memberId);

    /**
     * 删除成员
     *
     * @param groupId   群组id
     * @param memberIds 用户id
     */
    void removeMember(String groupId, Collection<String> memberIds);


    /**
     * 删除群组
     *
     * @param group 群组
     */
    void del(BaseSessionGroup group);
}
