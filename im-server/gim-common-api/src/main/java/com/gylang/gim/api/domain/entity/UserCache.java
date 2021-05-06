package com.gylang.gim.api.domain.entity;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/3
 */
@Data
public class UserCache {


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

    /** 创建时间 */
    private Long createTime;

    /** 有效期 */
    private Long expire = 60 * 60 * 18L;


    /** token */
    private String token;
}


