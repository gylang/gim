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

    private Map<String, IMSession> repository = new ConcurrentHashMap<>();

    @Override
    public IMSession find(String s) {
        return repository.get(s);
    }

    @Override
    public List<IMSession> findByIds(Collection<String> strings) {
        if (null == strings) {
            throw new IllegalArgumentException("keys is not empty");
        }
        List<IMSession> list = new ArrayList<>();
        for (String string : strings) {
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
    public Set<String> findAllKey() {
        return repository.keySet();
    }

    @Override
    public IMSession findByKey(String s) {
        return repository.get(s);
    }

    @Override
    public IMSession popByKey(String s) {
        return repository.remove(s);
    }

    @Override
    public IMSession pop(String s) {
        return repository.remove(s);
    }

    @Override
    public IMSession add(String s, IMSession session) {
        return repository.put(s, session);
    }
}
