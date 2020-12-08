package com.gylang.netty.sdk.coder;

/**
 * | 16 协议类型 | ****
 * 前16位用来标识协议类型, 用来确定使用那种协议解析器
 *
 * @author gylang
 * data 2020/12/7
 */
public interface BaseProtocolProcess<S, T> {

    /**
     * 解码
     *
     * @param s 带解密源
     * @return 解码结果
     */
    T decode(S s);


    /**
     * 转码
     *
     * @param t 转码对象
     * @return 转码结果
     */
    S encode(T t);
}
