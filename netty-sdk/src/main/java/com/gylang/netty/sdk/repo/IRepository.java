package com.gylang.netty.sdk.repo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author gylang
 * data 2020/10/29
 * @version v0.0.1
 */
public interface IRepository<KEY, QUERY, ENTITY> {

    /**
     * 查找实体
     *
     * @param query 查询条件
     * @return 实体
     */
    ENTITY find(QUERY query);

    /**
     * 查找实体
     *
     * @param keys 关键字列表
     * @return 实体
     */
    List<ENTITY> findByIds(Collection<KEY> keys);
    /**
     * 查找所有实体
     *
     * @return 实体
     */
    Collection<ENTITY> findAll();
    /**
     * 查找关键字
     *
     * @return 实体
     */
    Set<KEY> findAllKey();

    /**
     * 通过key查询实体
     *
     * @param key key
     * @return 实体
     */
    ENTITY findByKey(KEY key);

    /**
     * 弹出一个实体
     *
     * @param key key
     * @return 被弹出的实体
     */
    ENTITY popByKey(KEY key);

    /**
     * 弹出一个实体
     *
     * @param query 弹出条件
     * @return 被弹出的实体
     */
    ENTITY pop(QUERY query);

    /**
     * 添加一个实体
     *
     * @param key    key
     * @param entity 实体
     * @return 被弹出的实体
     */
    ENTITY add(KEY key, ENTITY entity);

}
