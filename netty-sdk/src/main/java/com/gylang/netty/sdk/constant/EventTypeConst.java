package com.gylang.netty.sdk.constant;

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
    String OFFLINE_MSG_EVENT = "OFFLINE_MSG_EVENT";

    /** 上线事件 */
    String ONLINE_EVENT = "ONLINE_EVENT";

    /** 跨服消息推送 */
    String CROSS_SERVER_PUSH = "CROSS_SERVER_PUSH";
    /** 持久化事件 */
    String PERSISTENCE_EVENT = "PERSISTENCE_EVENT";
}
