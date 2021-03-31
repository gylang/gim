package com.gylang.im.web.dto.request;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/4
 */
@Data
public class RegistryRequest {

    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 电话号码 */
    private String tel;
    /** 邮箱 */
    private String email;
    /** 昵称 */
    private String nickname;

}
