package com.gylang.gim.web.client.util.Store;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/31
 */
@Data
public class UserStore {

    private static final UserStore u = new UserStore();

    private String token;

    private String nickname;

    private String username;

    private String uid;

    private UserStore() {
    }

    public static UserStore getInstance() {
        return u;
    }

}
