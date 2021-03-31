package com.gylang.netty.client.util.Store;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/31
 */
@Data
public class UserStore {

    private final static UserStore u = new UserStore();

    private String token;

    private UserStore() {
    }

    public static UserStore getInstance() {
        return u;
    }

}
