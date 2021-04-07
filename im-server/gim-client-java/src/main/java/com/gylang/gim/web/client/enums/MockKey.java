package com.gylang.gim.web.client.enums;

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

    PRIVATE_CHAT("chatList-", "单聊前缀"),
    USER_ITEM_LIST("userItemList", "用户列表");
    private final String key;

    private final String mark;

    ;
}
