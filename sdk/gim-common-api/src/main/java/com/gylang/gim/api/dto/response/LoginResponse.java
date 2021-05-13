package com.gylang.gim.api.dto.response;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/3
 */
@Data
public class LoginResponse {

    /** token 令牌 */
    private String token;
    /** 用户id */
    private String uid;
    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    private String imIp;

    private Integer imPort;

    private String imToken;

    private Long imExpire = 60 * 60 * 18L;
}
