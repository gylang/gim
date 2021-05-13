package com.gylang.gim.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author gylang
 * data 2021/3/27
 */
@Configuration
public class LuaConfig {

    @Bean
    public RedisScript<Boolean> wlcBlcCheck() {

        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/wlc_blc_check.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;

    }
}
