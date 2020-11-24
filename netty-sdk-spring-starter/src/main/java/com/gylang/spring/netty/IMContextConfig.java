package com.gylang.spring.netty;

import com.gylang.netty.sdk.DefaultIMContext;
import com.gylang.netty.sdk.IMContext;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    @Bean
    public ThreadPoolExecutor imExecutor() {
        return new ThreadPoolExecutor(50, 92, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new DefaultThreadFactory("im线程数池"));
    }
}
