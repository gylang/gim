package com.gylang.gim.api.domain.response;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/3
 */
@Data
public class LoginResponse {

    /** token 令牌 */
    private String token;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;
}
