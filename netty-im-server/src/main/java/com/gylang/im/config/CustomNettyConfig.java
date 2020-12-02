package com.gylang.im.config;

import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import io.netty.handler.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gylang
 * data 2020/11/24
 */
@Configuration
public class CustomNettyConfig {


    @Bean
    public NettyConfig nettyConfig() {

        NettyConfig nettyConfig = new NettyConfig();
        Map<String, Object> properties = new HashMap<>();
        properties.put(NettyConfigEnum.LOG_LEVEL.getName(), LogLevel.DEBUG);
        nettyConfig.setProperties(properties);
        return nettyConfig;
    }
}