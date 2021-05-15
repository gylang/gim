package com.gylang.gim.api.constant.cmd;

/**
 * @author gylang
 * data 2021/5/6
 */
public interface ManagerCmd {

    /**
     * 用户令牌管理
     */
    String USER_APPLY_FOR_TOKEN_MANAGER = "UAFTM";

    /** 用户管理 */
    String USER_MANAGER = "URMR";

    /** 黑白名单 */
    String BLACK_WHITE_LIST_MANAGER = "BWLM";

    /** 创建新群组 */
    String GROUP_MANAGER = "GM";

    /** 群禁言管理 */
    String GROUP_DISABLE_SEND_MSG_MANAGER = "GDSMM";
}
