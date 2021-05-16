package com.gylang.gim.server.repo;

import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.netty.sdk.api.domain.model.BaseSessionGroup;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author gylang
 * data 2021/5/15
 */
@Component
public class RedisGimGroupRepository implements GIMGroupSessionRepository {

    @Resource
    private RedisTemplate<String, BaseSessionGroup> redisTemplate;
    @Resource
    private RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public void add(BaseSessionGroup group) {
        redisTemplate.opsForValue().set(CacheConstant.GROUP_INFO + group.getGroupId(), group);
        addMember(group.getGroupId(), group.getMaster());
    }

    @Override
    public BaseSessionGroup findGroupInfo(String groupId) {
        return redisTemplate.opsForValue().get(CacheConstant.GROUP_INFO + groupId);
    }

    @Override
    public List<BaseSessionGroup> findGroups(Collection<String> groupIds) {

        return redisTemplate.opsForValue().multiGet(groupIds);
    }

    @Override
    public Boolean checkIsMember(String groupId, String memberId) {
        return stringRedisTemplate.opsForSet().isMember(CacheConstant.GROUP_LIST_PREFIX + groupId, memberId);
    }

    @Override
    public Set<String> getAllMemberIds(String groupId) {
        return stringRedisTemplate.opsForSet().members(groupId);
    }

    @Override
    public Set<String> getMemberIds(String groupId, Collection<String> memberIds) {
        return stringRedisTemplate.opsForSet().intersect(memberIds);
    }

    @Override
    public boolean addMember(String groupId, String memberId) {
        if (null == findGroupInfo(groupId)) {
            return false;
        }
        stringRedisTemplate.opsForSet().add(groupId, memberId);
        return true;
    }

    @Override
    public boolean addMember(String groupId, Collection<String> memberIds) {
        if (null == findGroupInfo(groupId)) {
            return false;
        }
        stringRedisTemplate.opsForSet().add(groupId, memberIds.toArray(new String[0]));
        return true;
    }

    @Override
    public void removeMember(String groupId, String memberId) {
        stringRedisTemplate.opsForSet().remove(groupId, memberId);
    }

    @Override
    public void removeMember(String groupId, Collection<String> memberIds) {
        stringRedisTemplate.opsForSet().remove(groupId, memberIds.toArray());
    }

    @Override
    public void del(BaseSessionGroup group) {
        String groupId = group.getGroupId();
        redisTemplate.delete(CacheConstant.GROUP_INFO + groupId);
        stringRedisTemplate.delete(CacheConstant.GROUP_LIST_PREFIX + groupId);

    }
}
