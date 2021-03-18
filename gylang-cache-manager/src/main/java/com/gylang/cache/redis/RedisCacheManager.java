package com.gylang.cache.redis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.gylang.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gylang
 * data 2021/1/5
 */
@Service
public class RedisCacheManager implements CacheManager {

    @Autowired
    private ValueOperations<String, Object> valueOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object t) {
        valueOperations.set(key, t);
    }

    @Override
    public void set(String key, Object t, long expire) {
        valueOperations.set(key, t, expire, TimeUnit.SECONDS);
    }

    @Override
    public Boolean setnx(String key, Object t, long expire) {
        return valueOperations.setIfAbsent(key, t, expire, TimeUnit.SECONDS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) valueOperations.get(key);
    }

    @Override
    public Long increase(String key, long interval) {
        return valueOperations.increment(key, interval);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key) {
        return (List<T>) listOperations.range(key, 0, -1);
    }

    @Override
    public <T> void setList(String key, Collection<T> list) {
        listOperations.leftPushAll(key, list);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getMap(String key) {
        return (Map<String, T>) hashOperations.entries(key);
    }

    @Override
    public <T> void setMap(String key, Map<String, T> data) {

        hashOperations.putAll(key, data);
    }

    @Override
    public <T> void setMapField(String key, String field, T data) {
        hashOperations.put(key, field, data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> mapMultiGet(String key, String... fieldKey) {
        if (ArrayUtil.isEmpty(fieldKey)) {
            return Collections.emptyList();
        }
        return (List<T>) hashOperations.multiGet(key, CollUtil.newArrayList(fieldKey));
    }

    @Override
    public boolean mapContainKey(String key, String... fieldKey) {
        List<Object> list = mapMultiGet(key, fieldKey);
        if (CollUtil.isEmpty(list)) {
            return false;
        } else {
            return fieldKey.length == CollUtil.removeNull(list).size();
        }
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
