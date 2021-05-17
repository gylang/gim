package com.gylang.gim.room;

import com.gylang.netty.sdk.api.domain.model.BaseSessionGroup;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import com.gylang.spring.netty.EnableIM;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/5/17
 */
@SpringBootApplication
@EnableIM
public class SingleRoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleRoomApplication.class);

    }
}
