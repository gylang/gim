package com.gylang.netty.sdk.repo;

import com.gylang.netty.sdk.annotation.IMRepository;
import com.gylang.netty.sdk.domain.model.GIMSession;

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

    private Map<String, GIMSession> repository = new ConcurrentHashMap<>();

    @Override
    public GIMSession find(String s) {
        return repository.get(s);
    }

    @Override
    public List<GIMSession> findByIds(Collection<String> strings) {
        if (null == strings) {
            throw new IllegalArgumentException("keys is not empty");
        }
        List<GIMSession> list = new ArrayList<>();
        for (String string : strings) {
            GIMSession session = repository.get(string);
            if (null != session) {
                list.add(session);
            }
        }
        return list;
    }

    @Override
    public Collection<GIMSession> findAll() {
        return repository.values();
    }

    @Override
    public Set<String> findAllKey() {
        return repository.keySet();
    }

    @Override
    public GIMSession findByKey(String s) {
        return repository.get(s);
    }

    @Override
    public GIMSession popByKey(String s) {
        return repository.remove(s);
    }

    @Override
    public GIMSession pop(String s) {
        return repository.remove(s);
    }

    @Override
    public GIMSession add(String s, GIMSession session) {
        return repository.put(s, session);
    }
}
