package com.gylang.gim.api.domain.admin;

import lombok.Data;

/**
 * @author gylang
 * data 2021/4/7
 */
@Data
public class AdminUser {

    private String username;

    private String password;

    private String name;

    private String userId;

    private boolean loginEvent;
}
