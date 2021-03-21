package com.gylang.im;

import com.gylang.cache.RedisCacheSupport;
import com.gylang.spring.netty.EnableIM;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gylang
 * data 2021/3/3
 */
@SpringBootApplication
@EnableIM
@RedisCacheSupport
@MapperScan("com.gylang.im.dao.mapper")

public class IMServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IMServerApplication.class, args);
    }
}