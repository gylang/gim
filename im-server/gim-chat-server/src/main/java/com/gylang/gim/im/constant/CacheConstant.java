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

    public static final String WHITE_LIST_CHECK = "IM:USER:CONFIG:WLC:";

    public static final String BLACK_LIST_CHECK = "IM:USER:CONFIG:BLC:";

    public static final String IM_USER_CONFIG = "IM:USER:CONFIG:";
}
