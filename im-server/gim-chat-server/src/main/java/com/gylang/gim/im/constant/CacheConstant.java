package com.gylang.gim.im.constant;

/**
 * @author gylang
 * data 2021/1/5
 */
public enum CacheConstant {
    ;
    /** 授权用户前缀 */
    public static final String AUTH_TOKEN_PREFIX = "USER:";
    /** 授权过期时间 */
    public static final long AUTH_TOKEN_EXPIRE = 60 * 60 * 6L;
    /** 好友列表缓存前缀 */
    public static final String USER_FRIEND_LIST_PREFIX = "FRIEND:";
    /** 用户群列表 */
    public static final String GROUP_LIST_PREFIX = "GROUP:";
    /** 用户群聊最新msgId */
    public static final String GROUP_LAST_MSG_ID = "GROUP_LAST_MSG_ID:";
    /** 个人私聊最新消息msgId */
    public static final String PRIVATE_LAST_MSG_ID = "PRIVATE_LAST_MSG_ID:";
    /** 用户白名单 */
    public static final String WHITE_LIST_CHECK = "IM:USER:CONFIG:WLC:";
    /** 用户黑名单 */
    public static final String BLACK_LIST_CHECK = "IM:USER:CONFIG:BLC:";
    /** 用户单聊配置 */
    public static final String IM_USER_CONFIG = "IM:USER:CONFIG:";
    /** 用户群聊中配置 */
    public static final String GROUP_CHAT_CONFIG = "IM:USER:GROUP:CHAT:";
    /** 单聊历史数据缓存前缀 */
    public static final String PRIVATE_CHAT_HISTORY = "IM:USER:PRIVATE:CHAT:";
    /** 群聊历史数据缓存前缀 */
    public static final String GROUP_CHAT_HISTORY = "IM:USER:GROUP:CHAT:";

}
