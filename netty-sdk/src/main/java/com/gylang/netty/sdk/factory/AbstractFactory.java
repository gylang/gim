package com.gylang.netty.sdk.factory;

/**
 * @author gylang
 * data 2020/11/18
 */
public interface AbstractFactory<K, T> {

    /**
     * 创建对象
     *
     * @param k 创建对象参数
     * @return 创建对象
     */
    T create(K k);
}
