package com.gylang.gim.server.repo;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.domain.UserLinkStatus;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.repo.GIMSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2021/5/11
 */
@Component
public class RedisGIMSessionRepository implements GIMSessionRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public GIMSession findUserId(String userId) {
        String status = redisTemplate.<String, String>opsForHash().get(CacheConstant.USER_LINK_STATUS, userId);
        UserLinkStatus userLinkStatus = JSON.parseObject(status, UserLinkStatus.class);
        if (null != userLinkStatus) {
            GIMSession session = new GIMSession();
            session.setAccount(userId);
            session.setServerIp(userLinkStatus.getIp());
            session.setChannel(userLinkStatus.getChannel());
            session.setStatus(userLinkStatus.getStatus());
            return session;
        }
        return null;
    }


    @Override
    public List<GIMSession> findByUserIds(Collection<String> userIds) {

        List<String> status = redisTemplate.<String, String>opsForHash().multiGet(CacheConstant.USER_LINK_STATUS, userIds);

        return status.stream()
                .map(s -> JSON.parseObject(s, UserLinkStatus.class))
                .filter(Objects::nonNull)
                .map(stat -> {
                    GIMSession session = new GIMSession();
                    session.setAccount(stat.getAccountId());
                    session.setServerIp(stat.getIp());
                    session.setChannel(stat.getChannel());
                    session.setStatus(stat.getStatus());
                    return session;
                }).collect(Collectors.toList());


    }

    @Override
    public Long removeByUserId(String userId) {
        return redisTemplate.<String, String>opsForHash().delete(CacheConstant.USER_LINK_STATUS, userId);
    }

    @Override
    public Long removeByUserIds(Collection<String> userIds) {
        return redisTemplate.<String, String>opsForHash().delete(CacheConstant.USER_LINK_STATUS, userIds);
    }

    @Override
    public void addSession(GIMSession session) {
        UserLinkStatus userLinkStatus = new UserLinkStatus();
        userLinkStatus.setStatus(session.getStatus());
        userLinkStatus.setChannel(session.getChannel());
        userLinkStatus.setIp(session.getServerIp());
        userLinkStatus.setAccountId(session.getAccount());
        redisTemplate.<String, String>opsForHash().put(CacheConstant.USER_LINK_STATUS, session.getAccount(), JSON.toJSONString(userLinkStatus));
    }

    @Override
    public void addSessionList(List<GIMSession> session) {

        UserLinkStatus template = new UserLinkStatus();
        Map<String, String> pushMap = session.stream()
                .collect(Collectors.toMap(GIMSession::getAccount, s -> {
                    template.setStatus(s.getStatus());
                    template.setChannel(s.getChannel());
                    template.setIp(s.getServerIp());
                    template.setAccountId(s.getAccount());
                    return JSON.toJSONString(template);
                }));

        redisTemplate.<String, String>opsForHash().putAll(CacheConstant.USER_LINK_STATUS, pushMap);

    }

    @Override
    public void updateStatus(String userId, Integer status) {

        GIMSession session = findUserId(userId);
        if (null != session) {
            session.setStatus(status);
            addSession(session);
        }
    }


}
