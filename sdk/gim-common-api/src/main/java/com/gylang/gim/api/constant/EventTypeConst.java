package com.gylang.gim.api.constant;

/**
 * 内置消息通知的常量
 * @author gylang
 * data 2020/11/7
 * @version v0.0.1
 */
public interface EventTypeConst {

    /**
     * 读空闲
     */
    String READER_IDLE = "READER_IDLE";
    /**
     * 写空闲
     */
    String WRITER_IDLE = "WRITER_IDLE";
    /**
     * 读写空闲
     */
    String ALL_IDLE = "ALL_IDLE";
    /**
     * 超时关闭
     */
    String OVER_TIME_CLOSE = "overTimeClose";

    /** 离线消息 */
    String PERSISTENCE_MSG_EVENT = "OFFLINE_MSG_EVENT";

    /** 上线事件 */
    String ONLINE_EVENT = "ONLINE_EVENT";

    /** 跨服消息推送 */
    String CROSS_SERVER_PUSH = "CROSS_SERVER_PUSH";

    /** 消息发送失败 */
    String SEND_MES_ERROR = "SEND_MES_ERROR";

    String QOS_SEND_MES_ERROR = "QOS_SEND_MES_ERROR";

    /** 未找到该用户 */
    String USER_NOT_FOUND = "USER_NOT_FOUND";

    String USER_ONLINE = "USER_ONLINE";

    String COLSE_CONNECT = "COLSE_CONNECT";
}
