package com.gylang.netty.sdk.repo;

import com.gylang.netty.sdk.annotation.IMRepository;
import com.gylang.netty.sdk.domain.model.IMSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话仓库
 *
 * @author gylang
 * data 2020/8/28
 * @version v0.0.1
 */
@IMRepository
public class DefaultIMRepository implements IMSessionRepository {

    private Map<Long, IMSession> repository = new ConcurrentHashMap<>();

    @Override
    public IMSession find(Long s) {
        return repository.get(s);
    }

    @Override
    public List<IMSession> findByIds(Collection<Long> strings) {
        if (null == strings) {
            throw new IllegalArgumentException("keys is not empty");
        }
        List<IMSession> list = new ArrayList<>();
        for (Long string : strings) {
            IMSession session = repository.get(string);
            if (null != session) {
                list.add(session);
            }
        }
        return list;
    }

    @Override
    public Collection<IMSession> findAll() {
        return repository.values();
    }

    @Override
    public Set<Long> findAllKey() {
        return repository.keySet();
    }

    @Override
    public IMSession findByKey(Long s) {
        return repository.get(s);
    }

    @Override
    public IMSession popByKey(Long s) {
        return repository.remove(s);
    }

    @Override
    public IMSession pop(Long s) {
        return repository.remove(s);
    }

    @Override
    public IMSession add(Long s, IMSession session) {
        return repository.put(s, session);
    }
}
