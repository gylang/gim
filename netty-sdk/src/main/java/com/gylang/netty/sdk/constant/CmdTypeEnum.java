package com.gylang.netty.sdk.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指令类型操作类
 *
 * @author gylang
 * data 2021/3/26
 */
@Getter
@AllArgsConstructor
public enum CmdTypeEnum {

    /** 聊天类型 */
    PRIVATE_CHAT(1, "单聊"),
    GROUP_CHAT(1, "组聊"),
    NOTIFY(1, "通知类"),
    CHAT_ROOM(1, "聊天室"),
    ;

    private final int cmd;

    private final String mark;
}
