package com.gylang.im.im.constant;

/**
 * @author gylang
 * data 2021/3/6
 */
public interface BizChatCmd {

    /** 消息私发 */
    String PRIVATE_CHAT = "PRIVATE_CHAT";

    /** 群消息 */
    String GROUP_CHAT = "GROUP_CHAT";

    /** 好友申请通知 */
    String APPLY_FRIEND_NOTIFY = "APPLY_FRIEND_NOTIFY";

    /** 系统通知 */
    String SYSTEM_NOTIFY = "SYSTEM_NOTIFY";
    /** 连接socket成功 */
    String SOCKET_CONNECTED = "SOCKET_CONNECTED";
    /** 登录 */
    String LOGIN_SOCKET = "LOGIN_SOCKET";

}
