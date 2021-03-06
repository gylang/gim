package com.gylang.netty.sdk.api.conveter;

import com.gylang.gim.api.domain.common.MessageWrap;

/**
 *
 * todo 实体转化, 用于protobuf 或json等序列化之间进行切换
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */

public interface DataConverter {
    /**
     * 转成具体实体类型
     *
     * @param clazz       类型
     * @param messageWrap 统一消息封装类型
     * @return 实体类型
     */
    Object converterTo(Class<?> clazz, MessageWrap messageWrap);

    /**
     * 转成具体实体类型
     *
     * @param clazz       类型
     * @param content 消息体
     * @return 实体类型
     */
    Object converterTo(Class<?> clazz, String content);

    /**
     * 转成具体实体类型
     *
     * @param clazz       类型
     * @param content 消息体
     * @return 实体类型
     */
    Object converterTo(Class<?> clazz, byte[] content);

    /**
     * 统一序列化方法
     *
     * @param object 带序列化类型
     * @return 序列化后的byte[]
     */
    <T, S> S encode(T object);
}
