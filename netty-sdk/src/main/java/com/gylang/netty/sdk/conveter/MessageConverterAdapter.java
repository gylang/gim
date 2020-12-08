package com.gylang.netty.sdk.conveter;

/**
 * @author gylang
 * data 2020/11/27
 * @version v0.0.1
 */
public interface MessageConverterAdapter<S> {
    /**
     * 判断是否支持该格式数据的解析
     *
     * @param wrap  数据包装类
     * @param clazz 解析后的类型
     * @return 是否支持
     */
    boolean support(S wrap, Class<?> clazz);

    /**
     * 处理解析
     *
     * @param wrap  数据包装类
     * @param clazz 解析后的接口类型
     * @param <T>   类型
     * @return 解析后的对象
     */
    <T> T resolve(S wrap, Class<T> clazz);
}
