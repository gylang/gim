package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.remote.SocketManager;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SocketManager socketManager;

    /**
     * 测试发送信息
     * @param messageWrap
     * @return
     */
    @RequestMapping("sendAllMsg")
    public CommonResult<Boolean> sendAllMsg(@RequestBody MessageWrap messageWrap) {
        socketManager.send(messageWrap);
        return CommonResult.ok();
    }
}
