package com.gylang.netty.client.domain.request;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/3
 */
@Data
public class LoginRequest {

    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;

}
