package com.gylang.gim.im;

import com.alibaba.fastjson.JSON;
import com.gylang.cache.RedisCacheSupport;
import com.gylang.spring.netty.EnableIM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author gylang
 * data 2021/3/26
 */
@EnableIM
@RedisCacheSupport
@SpringBootApplication
@Slf4j
public class IMApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(IMApplication.class);
        log.info("容器启动完成：共计加载了： {}", JSON.toJSONString(applicationContext.getBeanDefinitionNames()));
    }
}
