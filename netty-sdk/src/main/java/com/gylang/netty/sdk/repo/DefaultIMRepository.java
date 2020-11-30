package com.gylang.netty.sdk.repo;

import com.gylang.netty.sdk.annotation.IMRepository;
import com.gylang.netty.sdk.domain.model.IMSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话仓库
 *
 * @author gylang
 * data 2020/8/28
 * @version v0.0.1
 */
@IMRepository
public class DefaultIMRepository implements IMSessionReopistry<String, String, IMSession> {

    private Map<String, IMSession> repository = new ConcurrentHashMap<>();

    @Override
    public IMSession find(String s) {
        return repository.get(s);
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
