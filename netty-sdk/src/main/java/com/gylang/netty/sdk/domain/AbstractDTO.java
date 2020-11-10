package com.gylang.netty.sdk.domain;

/**
 * @author gylang
 * data 2020/11/4
 * @version v0.0.1
 */
public abstract class AbstractDTO<E> {

    public AbstractDTO() {
    }

    /**
     * 抽象实现 用于后续切换 json protobuf
     *
     * @param dto 包装对象
     */
    public abstract E parseFrom(MessageWrap dto);

    public abstract void decode();
}
