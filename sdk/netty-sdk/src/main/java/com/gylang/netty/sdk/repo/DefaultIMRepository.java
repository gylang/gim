package com.gylang.netty.sdk.repo;

import com.gylang.netty.sdk.api.annotation.IMRepository;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.repo.GIMSessionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class DefaultIMRepository implements GIMSessionRepository {


    private Map<String, GIMSession> repository = new ConcurrentHashMap<>();

    @Override
    public GIMSession findUserId(String s) {
        return repository.get(s);
    }

    @Override
    public List<GIMSession> findByUserIds(Collection<String> strings) {
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


    public GIMSession addSession(String s, GIMSession session) {
        return repository.put(s, session);
    }

    @Override
    public Long removeByUserId(String userId) {
        return null == repository.remove(userId) ? 0L :1L;
    }

    @Override
    public Long removeByUserIds(Collection<String> userIds) {

        return userIds.stream().mapToLong(this::removeByUserId).sum();
    }

    @Override
    public void addSession(GIMSession session) {
        repository.put(session.getAccount(), session);
    }

    @Override
    public void addSessionList(List<GIMSession> session) {
        session.forEach(this::addSession);
    }

    @Override
    public void updateStatus(String userId, Integer status) {
        GIMSession session = repository.get(userId);
        if (null != session) {
            session.setStatus(status);
        }
    }
}
