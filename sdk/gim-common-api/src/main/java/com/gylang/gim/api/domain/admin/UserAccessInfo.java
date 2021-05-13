package com.gylang.gim.api.domain.admin;

import lombok.Data;

/**
 * @author gylang
 * data 2021/5/11
 */
@Data
public class UserAccessInfo {

    private String id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String tel;

    /**
     * 昵称
     */
    private String nickname;

    /** 当前状态 */
    private Integer status;

}
