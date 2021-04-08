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

    static {
        HEART.setType(ChatTypeEnum.SYSTEM_MESSAGE.getType());
    }
}
