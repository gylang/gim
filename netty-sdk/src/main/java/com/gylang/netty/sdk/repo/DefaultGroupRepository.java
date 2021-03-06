package com.gylang.netty.sdk.repo;

import com.gylang.netty.sdk.annotation.IMGroupRepository;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 会话组仓库
 *
 * @author gylang
 * data 2020/10/29
 * @version v0.0.1
 */
@IMGroupRepository
public class DefaultGroupRepository implements IMGroupSessionRepository {

    Map<Long, AbstractSessionGroup> groupMap = new ConcurrentHashMap<>();

    @Override
    public AbstractSessionGroup find(AbstractSessionGroup sessionGroup) {
        if (null == sessionGroup) {
            return null;
        }
        return groupMap.get(sessionGroup.getKey());
    }

    @Override
    public List<AbstractSessionGroup> findByIds(Collection<Long> strings) {
        if (null == strings) {
            throw new IllegalArgumentException("keys is not empty");
        }
        List<AbstractSessionGroup> list = new ArrayList<>();
        for (Long string : strings) {
            AbstractSessionGroup abstractSessionGroup = groupMap.get(string);
            if (null != abstractSessionGroup) {
                list.add(abstractSessionGroup);
            }
        }
        return list;
    }

    @Override
    public Collection<AbstractSessionGroup> findAll() {
        return groupMap.values();
    }

    @Override
    public Set<Long> findAllKey() {
        return groupMap.keySet();
    }

    @Override
    public AbstractSessionGroup findByKey(Long s) {
        return groupMap.get(s);
    }

    @Override
    public AbstractSessionGroup popByKey(Long s) {
        return groupMap.remove(s);
    }

    @Override
    public AbstractSessionGroup pop(AbstractSessionGroup sessionGroup) {
        return groupMap.remove(sessionGroup);
    }

    @Override
    public AbstractSessionGroup add(Long s, AbstractSessionGroup group) {
        return groupMap.put(s, group);
    }
}
