package com.gylang.gim.web.config;

import com.gylang.gim.api.constant.CommonConstant;
import com.gylang.gim.remote.SocketHolder;
import com.gylang.gim.remote.SocketManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gylang
 * data 2021/4/7
 */
@Configuration
@Slf4j
public class GimConfig {

    @Value("${gim.port}")
    private Integer port;
    @Value("${gim.ip}")
    private String ip;
    @Value("${gim.username}")
    private String username;
    @Value("${gim.password}")
    private String password;

    @Bean
    public SocketManager socketManager() {
        SocketManager socketManager = SocketHolder.getInstance();
        socketManager.connect(ip, port, username, password, s -> {

            if (CommonConstant.TRUE_INT_STR.equals(s)) {
                log.info("[gim 连接] : 连接成功");
            } else {
                log.info("[gim 连接] : (重)连接失败");
            }
        });
        return socketManager;
    }

}
