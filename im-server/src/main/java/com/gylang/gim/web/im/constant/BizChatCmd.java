package com.gylang.gim.web.im.constant;

/**
 * @author gylang
 * data 2021/3/6
 */
public enum BizChatCmd {
    ;
    /** 消息私发 */
    public static final String PRIVATE_CHAT = "PRIVATE_CHAT";

    /** 群消息 */
    public static final String GROUP_CHAT = "GROUP_CHAT";

    /** 好友申请通知 */
    public static final String APPLY_FRIEND_NOTIFY = "APPLY_FRIEND_NOTIFY";

    /** 系统通知 */
    public static final String SYSTEM_NOTIFY = "SYSTEM_NOTIFY";

    /** 连接socket成功 */
    public static final String SOCKET_CONNECTED = "SOCKET_CONNECTED";

    /** 登录 */
    public static final String LOGIN_SOCKET = "LOGIN_SOCKET";
    /** 更新私聊最新 msgId */
    public static final String PRIVATE_CHAT_LAST_MSG_ID = "PRIVATE_CHAT_LAST_MSG_ID";
    /** 更新群聊最新 msgId */
    public static final String GROUP_CHAT_LAST_MSG_ID = "GROUP_CHAT_LAST_MSG_ID";

}
