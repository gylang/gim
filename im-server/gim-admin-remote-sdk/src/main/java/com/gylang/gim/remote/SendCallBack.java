package com.gylang.gim.remote;

import com.gylang.gim.api.domain.MessageWrap;

/**
 * @author gylang
 * data 2021/4/2
 */
public interface SendCallBack {

    /**
     * 回调
     * @param msg
     */
    void callBack(MessageWrap msg);
}
