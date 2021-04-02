package com.gylang.netty.client.config;

import lombok.Data;

/**
 * @author gylang
 * data 2021/4/2
 */
@Data
public class ClientConfig {

    private ClientConfig() {

    }

    private static final ClientConfig config = new ClientConfig();

    public static ClientConfig getInstance() {
        return config;
    }

    private String env;

    private String mockPath;
}
