package com.gylang.gim.api.constant.cmd;

/**
 * @author gylang
 * data 2021/3/6
 */
public enum PrivateChatCmd {
    ;


    /** 连接socket成功 */
    public static final String SOCKET_CONNECTED = "STCD";


    /** 更新私聊最新 msgId */
    public static final String PRIVATE_CHAT_LAST_MSG_ID = "PCLMI";
    /** 更新群聊最新 msgId */
    public static final String GROUP_CHAT_LAST_MSG_ID = "GCLMI";

    public static final String SIMPLE_PRIVATE_CHAT = "SPC";

}
