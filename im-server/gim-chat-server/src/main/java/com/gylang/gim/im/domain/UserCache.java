package com.gylang.gim.im.domain;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/3
 */
@Data
public class UserCache {


    private Long id;
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

    /**
     * 头像
     */
    private String avatar;


    /**
     * 简介
     */
    private String intro;
}
