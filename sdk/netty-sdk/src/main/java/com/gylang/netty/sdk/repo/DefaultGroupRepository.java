package com.gylang.netty.sdk.repo;

import cn.hutool.core.util.ObjectUtil;
import com.gylang.netty.sdk.api.annotation.IMGroupRepository;
import com.gylang.netty.sdk.api.domain.model.BaseSessionGroup;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;


/**
 * 会话组仓库
 *
 * @author gylang
 * data 2020/10/29
 * @version v0.0.1
 */
@IMGroupRepository
public class DefaultGroupRepository implements GIMGroupSessionRepository {

    Map<String, BaseSessionGroup> groupMap = new ConcurrentHashMap<>();

    Map<String, Set<String>> membersMap = new ConcurrentHashMap<>();

    @Override
    public void add(BaseSessionGroup group) {
        groupMap.put(group.getGroupId(), group);
    }

    @Override
    public BaseSessionGroup findGroupInfo(String groupId) {
        if (null == groupId) {
            return null;
        }
        return groupMap.get(groupId);
    }

    @Override
    public List<BaseSessionGroup> findGroups(Collection<String> groupIds) {
        if (null == groupIds) {
            throw new IllegalArgumentException("keys is not empty");
        }
        List<BaseSessionGroup> list = new ArrayList<>();
        for (String groupId : groupIds) {
            BaseSessionGroup baseSessionGroup = groupMap.get(groupId);
            if (null != baseSessionGroup) {
                list.add(baseSessionGroup);
            }
        }
        return list;
    }

    @Override
    public Boolean checkIsMember(String groupId, String memberId) {
        Set<String> memberMap = membersMap.get(groupId);
        return null != memberMap && memberMap.contains(memberId);
    }

    @Override
    public Set<String> getAllMemberIds(String groupId) {
        Set<String> members = membersMap.get(groupId);
        return ObjectUtil.defaultIfNull(members, new HashSet<>());
    }

    @Override
    public Set<String> getMemberIds(String groupId, Collection<String> memberIds) {
        Set<String> members = membersMap.get(groupId);
        return memberIds.stream().filter(members::contains).collect(Collectors.toSet());
    }

    @Override
    public boolean addMember(String groupId, String memberId) {
        if (groupMap.containsKey(groupId)) {
            Set<String> members = membersMap
                    .computeIfAbsent(groupId, k -> new ConcurrentSkipListSet<>());
            members.add(memberId);
            return true;
        }
        return false;
    }

    @Override
    public boolean addMember(String groupId, Collection<String> memberIds) {
        if (groupMap.containsKey(groupId)) {
            Set<String> members = membersMap
                    .computeIfAbsent(groupId, k -> new ConcurrentSkipListSet<>());
            members.addAll(memberIds);
            return true;
        }
        return false;
    }

    @Override
    public void removeMember(String groupId, String memberId) {
        if (groupMap.containsKey(groupId)) {
            Set<String> members = membersMap.get(groupId);
            if (null != members) {
                members.remove(memberId);
            }
        }
    }

    @Override
    public void removeMember(String groupId, Collection<String> memberIds) {
        if (groupMap.containsKey(groupId)) {
            Set<String> members = membersMap.get(groupId);
            if (null != members) {
                members.removeAll(memberIds);
            }
        }
    }

    @Override
    public void del(BaseSessionGroup group) {
        String groupId = group.getGroupId();
        groupMap.remove(groupId);
        membersMap.remove(groupId);
    }
}
