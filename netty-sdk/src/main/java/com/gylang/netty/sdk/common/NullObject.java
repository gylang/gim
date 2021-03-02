package com.gylang.netty.sdk.common;

/**
 * @author gylang
 * data 2021/2/21
 */
public class NullObject {
    public static final NullObject NULL_OBJECT = new NullObject();
    private NullObject() {}

    public static NullObject getInstance() {
        return NULL_OBJECT;
    }
}
