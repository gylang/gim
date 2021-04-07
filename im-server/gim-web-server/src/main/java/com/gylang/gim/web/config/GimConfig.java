package com.gylang.gim.web.config;

import com.gylang.gim.remote.SocketHolder;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.remote.call.GimCallBack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gylang
 * data 2021/4/7
 */
@Configuration
public class GimConfig {

    @Value("gim.port")
    private Integer port;
    @Value("gim.ip")
    private String ip;
    @Value("gim.username")
    private String username;
    @Value("gim.password")
    private String password;

    @Bean
    public SocketManager socketManager() {
        SocketManager socketManager = SocketHolder.getInstance();
        socketManager.connect(ip, port, username, password, new GimCallBack<String>() {
            @Override
            public void call(String s) {

            }
        });
        return socketManager;
    }

}
