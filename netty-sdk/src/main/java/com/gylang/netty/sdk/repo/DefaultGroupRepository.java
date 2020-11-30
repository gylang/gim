package com.gylang.netty.sdk.repo;

import com.gylang.netty.sdk.annotation.IMGroupRepository;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 会话组仓库
 *
 * @author gylang
 * data 2020/10/29
 * @version v0.0.1
 */
@IMGroupRepository
public class DefaultGroupRepository implements IMGroupReopistry {

    Map<String, AbstractSessionGroup> groupMap = new ConcurrentHashMap<>();

    @Override
    public AbstractSessionGroup find(AbstractSessionGroup sessionGroup) {
        if (null == sessionGroup) {
            return null;
        }
        return groupMap.get(sessionGroup.getKey());
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
        return groupMap.remove(sessionGroup);
    }

    @Override
    public AbstractSessionGroup add(String s, AbstractSessionGroup group) {
        return groupMap.put(s, group);
    }
}
