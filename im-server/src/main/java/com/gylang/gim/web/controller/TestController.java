package com.gylang.gim.web.controller;

import com.gylang.gim.web.api.domain.common.CommonResult;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/7
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private IMSessionRepository sessionRepository;

    @RequestMapping("sendAllMsg")
    public CommonResult<Boolean> sendAllMsg(@RequestBody MessageWrap messageWrap) {
        sessionRepository.findAll().forEach(session -> session.getSession().writeAndFlush(messageWrap));
        return CommonResult.ok();
    }
}
