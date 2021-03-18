package com.gylang.im;

import com.gylang.spring.netty.EnableIM;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gylang
 * data 2020/11/16
 */
@SpringBootApplication
@EnableIM
public class IMApplication {

    public static void main(String[] args) {

        SpringApplication.run(IMApplication.class, args);
    }
}
