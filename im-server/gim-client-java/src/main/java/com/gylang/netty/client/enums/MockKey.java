package com.gylang.netty.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gylang
 * data 2021/4/2
 */
@AllArgsConstructor
@Getter
public enum MockKey {

    /** mock数据key */

    PRIVATE_CHAT("chatList-", "单聊前缀");
    private final String key;

    private final String mark;

    ;
}
