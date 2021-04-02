package com.gylang.netty.client.socket;

import com.gylang.im.api.domain.MessageWrap;

/**
 * @author gylang
 * data 2021/4/2
 */
public interface SendCallBack {

    void callBack(MessageWrap msg);
}
