package com.gylang.gim.data;

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
@MapperScan("com.gylang.gim.data.mapper")
public class DataCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCenterApplication.class, args);
    }
}
