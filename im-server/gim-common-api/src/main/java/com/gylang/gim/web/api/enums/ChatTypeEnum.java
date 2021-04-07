package com.gylang.gim.web.api.enums;

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
public enum ChatTypeEnum {

    /** 聊天类型 */
    SYSTEM_MESSAGE(-1, "系统类消息"),
    PRIVATE_CHAT(1, "单聊"),
    GROUP_CHAT(2, "组聊"),
    NOTIFY(3, "通知类"),
    CHAT_ROOM(4, "聊天室"),
    ;

    private final int type;

    private final String mark;
}
