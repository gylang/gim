package com.gylang.gim.remote.call;

/**
 * @author gylang
 * data 2021/3/30
 */
public interface GimCallBack<T> {

    /**
     * 回调
     *
     * @param t 回调数据
     */
    void call(T t);
}
