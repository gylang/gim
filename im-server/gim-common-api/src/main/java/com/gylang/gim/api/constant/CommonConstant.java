package com.gylang.gim.api.constant;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;

/**
 * @author gylang
 * data 2021/1/4
 */
public enum CommonConstant {
    ;
    /** 空字符串 */
    public static final String EMPTY_STR = "";

    /** 空字符串 */
    public static final String SPLIT_STR = "|";

    /** 标识时间的永久预设标识 */
    public static final long EVER_TIME = -1;

    /** 数值标识成功 true的效果 */
    public static final String TRUE_INT_STR = "1";

    /** 数值标识失败 false的效果 */
    public static final String FALSE_INT_STR = "0";
    public static final int FALSE_INT = 0;
    public static final int TRUE_INT = 1;

    /** 正常状态 */
    public static final String NORMAL = "1";

    public static final int NORMAL_INT = 1;

    public static final String TOKEN_HEADER = "token";

    /** 白名单校验 */
    public static final String WHITE_LIST_CHECK = "WLC";

    /** 黑名单校验 */
    public static final String BLACK_LIST_CHECK = "BLC";

    public static final String PRIVATE_CHAT_ACCESS_CHECK = "PRIVATE_CHAT_ACCESS_CHECK";

    public static final MessageWrap HEART = new MessageWrap();
    /** 系统发送 */
    public static final String SYSTEM_SENDER = "0";
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
    static {
        HEART.setType(ChatTypeEnum.SYSTEM_MESSAGE.getType());
    }
}
