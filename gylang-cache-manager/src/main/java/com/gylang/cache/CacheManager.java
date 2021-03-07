package com.gylang.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 抽象缓存管理, 方便切换缓存策略
 *
 * @author gylang
 * data 2021/1/4
 */
public interface CacheManager {


    /**
     * 永久缓存
     *
     * @param t   缓存数据
     * @param key 缓存key
     */
    void set(String key, Object t);

    /**
     * 超时缓存
     *
     * @param key    缓存key
     * @param t      缓存数据
     * @param expire 过期时间
     */
    void set(String key, Object t, long expire);

    /**
     * 超时缓存
     *
     * @param key    缓存key
     * @param t      缓存数据
     * @param expire 过期时间
     * @return 尝试设置缓存 成功返回true
     */
    Boolean setnx(String key, Object t, long expire);

    /**
     * 获取缓存数据
     *
     * @param key key
     * @param <T> 数据类型
     * @return 缓存数据
     */
    <T> T get(String key);

    /**
     * 递增器
     *
     * @param key      key
     * @param interval 间隔
     * @return 递增结果
     */
    Long increase(String key, long interval);

    /**
     * 获取一个list
     *
     * @param key key
     * @param <T> 数值
     * @return lsit
     */
    <T> List<T> getList(String key);

    /**
     * 添加一个list
     *
     * @param key key
     * @param <T> 数值
     */
    <T> void setList(String key, Collection<T> list);

    /**
     * 获取一个map
     *
     * @param key key
     * @param <T> value类型
     * @return map
     */
    <T> Map<String, T> getMap(String key);

    /**
     * 添加一个map
     *
     * @param key  key
     * @param data 数值
     * @param <T>  数值
     */
    <T> void setMap(String key, Map<String, T> data);

    /**
     * 判断map中是否包含当前 fieldKey
     *
     * @param key      map key
     * @param fieldKey fieldkey
     * @return true 全包含
     */
    boolean mapContainKey(String key, String... fieldKey);

    /**
     * 获取map中的entry
     *
     * @param key      map key
     * @param fieldKey fieldkey
     * @return true 全包含
     */
    <T> List<T> mapMultiGet(String key, String... fieldKey);

    void delete(String key);
}
