package com.gylang.netty.sdk.common;

/**
 * @author gylang
 * data 2021/2/21
 */
public class NlllSuccess {
    public static final NlllSuccess NULL_OBJECT = new NlllSuccess();
    private NlllSuccess() {}

    public static NlllSuccess getInstance() {
        return NULL_OBJECT;
    }
}
