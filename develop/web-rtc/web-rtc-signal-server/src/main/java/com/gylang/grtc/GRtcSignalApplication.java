package com.gylang.grtc;

import com.gylang.spring.netty.EnableIM;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gylang
 * data 2021/4/23
 */
@SpringBootApplication
@EnableIM
public class GRtcSignalApplication {

    public static void main(String[] args) {
        SpringApplication.run(GRtcSignalApplication.class, args);
    }
}
