package com.gylang.gim.im;

import com.gylang.spring.netty.EnableIM;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author gylang
 * data 2021/3/26
 */
@EnableIM
@SpringBootApplication
public class IMApplication {

    public static void main(String[] args) {

        SpringApplication.run(IMApplication.class);
    }
}
