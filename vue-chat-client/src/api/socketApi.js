export default {
    // 消息私发
    PRIVATE_CHAT: "PRIVATE_CHAT",
    // 群消息
    GROUP_CHAT: "GROUP_CHAT",
    // 好友申请通知
    APPLY_FRIEND_NOTIFY: "APPLY_FRIEND_NOTIFY",
    // 系统通知
    SYSTEM_NOTIFY: "SYSTEM_NOTIFY",
    // 连接socket成功
    SOCKET_CONNECTED: "SOCKET_CONNECTED",
    // 登录
    LOGIN_SOCKET: "LOGIN_SOCKET",

    type : {
        QOS_SEND_ACK : -1,
        QOS_RECEIVE_ACK : -2,
        NOTIFY : 1,
        BIZ_MSG : 2,
        ERROR_MSG : 3,
    }
}