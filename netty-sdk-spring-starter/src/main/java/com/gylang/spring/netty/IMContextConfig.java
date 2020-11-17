package com.gylang.spring.netty;

import com.gylang.netty.sdk.DefaultIMContext;
import com.gylang.netty.sdk.IMContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gylang
 * data 2020/11/17
 */
@Configuration
public class IMContextConfig {


    @Bean
    public IMContext imContext() {
        return new DefaultIMContext();
    }
}
