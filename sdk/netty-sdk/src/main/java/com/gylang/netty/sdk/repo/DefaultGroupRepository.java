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

    Map<String, AbstractSessionGroup> groupMap = new ConcurrentHashMap<>();

    @Override
    public AbstractSessionGroup findUserId(AbstractSessionGroup sessionGroup) {
        if (null == sessionGroup) {
            return null;
        }
        return groupMap.get(sessionGroup.getKey());
    }

    @Override
    public List<AbstractSessionGroup> findByUserIds(Collection<String> strings) {
        if (null == strings) {
            throw new IllegalArgumentException("keys is not empty");
        }
        List<AbstractSessionGroup> list = new ArrayList<>();
        for (String string : strings) {
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
    public Set<String> findAllKey() {
        return groupMap.keySet();
    }

    @Override
    public AbstractSessionGroup findByKey(String s) {
        return groupMap.get(s);
    }

    @Override
    public AbstractSessionGroup popByKey(String s) {
        return groupMap.remove(s);
    }

    @Override
    public AbstractSessionGroup pop(AbstractSessionGroup sessionGroup) {
        return groupMap.remove(sessionGroup.getKey());
    }

    @Override
    public AbstractSessionGroup add(String s, AbstractSessionGroup group) {
        return groupMap.put(s, group);
    }
}
