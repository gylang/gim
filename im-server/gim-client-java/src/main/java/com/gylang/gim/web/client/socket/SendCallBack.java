package com.gylang.gim.web.client.socket;

import com.gylang.gim.web.api.domain.MessageWrap;

/**
 * @author gylang
 * data 2021/4/2
 */
public interface SendCallBack {

    void callBack(MessageWrap msg);
}
