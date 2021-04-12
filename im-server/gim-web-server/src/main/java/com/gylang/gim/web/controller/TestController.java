package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gylang
 * data 2021/3/7
 */
@RestController
@RequestMapping("test")
public class TestController {

    private IMSessionRepository sessionRepository;

    @RequestMapping("sendAllMsg")
    public CommonResult<Boolean> sendAllMsg(@RequestBody MessageWrap messageWrap) {
        sessionRepository.findAll().forEach(session -> session.getSession().writeAndFlush(messageWrap));
        return CommonResult.ok();
    }
}