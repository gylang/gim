package com.gylang.netty.sdk.repo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 抽象会话管理
 * @author gylang
 * data 2020/10/29
 * @version v0.0.1
 */
public interface IRepository<K, R, T> {

    /**
     * 查找实体
     *
     * @param query 查询条件
     * @return 实体
     */
    T find(R query);

    /**
     * 查找实体
     *
     * @param keys 关键字列表
     * @return 实体
     */
    List<T> findByIds(Collection<K> keys);
    /**
     * 查找所有实体
     *
     * @return 实体
     */
    Collection<T> findAll();
    /**
     * 查找关键字
     *
     * @return 实体
     */
    Set<K> findAllKey();

    /**
     * 通过key查询实体
     *
     * @param key key
     * @return 实体
     */
    T findByKey(K key);

    /**
     * 弹出一个实体
     *
     * @param key key
     * @return 被弹出的实体
     */
    T popByKey(K key);

    /**
     * 弹出一个实体
     *
     * @param query 弹出条件
     * @return 被弹出的实体
     */
    T pop(R query);

    /**
     * 添加一个实体
     *
     * @param key    key
     * @param entity 实体
     * @return 被弹出的实体
     */
    T add(K key, T entity);

}
