package com.gylang.gim.api.enums;

/**
 * 指令类型操作类
 *
 * @author gylang
 * data 2021/3/26
 */
public interface ChatType {

    int QOS_SERVER_SEND_ACK = -4;

    int QOS_CLIENT_SEND_ACK = -3;

    int HEART = -2;
    /** 系统类消息 */
    int SYSTEM_MESSAGE = -1;
    /** 单聊 */
    int PRIVATE_CHAT = 1;
    /** 组聊 */
    int GROUP_CHAT = 2;
    /** 通知类 */
    int NOTIFY_CHAT = 3;
    /** 聊天室 */
    int CHAT_ROOM = 4;
    /** 推送服务类 */
    int PUSH_CHAT = 5;
    /** 回复类 */
    int REPLY_CHAT = 6;
    /** 授权类 */
    int CLIENT_AUTH = 7;

    int GROUP_CHAT_LAST_MSG_ID = 8;

    int PRIVATE_CHAT_LAST_MSG_ID = 9;

    int REMOTE_LOGIN = 10;

    int P2G_PUSH = 11;

    int P2P_PUSH = 12;

    int MANAGER = 13;


}
