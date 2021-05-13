package com.gylang.spring.netty.custom.reslove;

/**
 * 抽象解析器
 *
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
public interface AbstractResolver<S, T> {
    /**
     * 判断是否支持解析类型
     *
     * @param s 带解析类型
     * @return true 可以支持
     */
    boolean support(S s);

    /**
     * 解析成具体对象
     *
     * @param s 带解析对象
     * @return 解析结果
     */
    T resolve(S s);
}
