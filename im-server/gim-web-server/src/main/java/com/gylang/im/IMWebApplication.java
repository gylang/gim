package com.gylang.im;

import com.gylang.cache.RedisCacheSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gylang
 * data 2021/3/3
 */
@SpringBootApplication
@RedisCacheSupport
@MapperScan("com.gylang.im.mapper")
public class IMWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(IMWebApplication.class, args);
    }
}
