package com.gylang.gim.web.server.service.impl;

import com.gylang.gim.web.server.BaseTest;
import com.gylang.gim.web.server.service.SendAccessService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author gylang
 * data 2021/3/27
 */
@Slf4j
public class SendAccessServiceImplTest extends BaseTest {

    @Autowired
    private SendAccessService sendAccessService;

    @Test
    public void privateAccessCheck() {
        log.info("执行结果: {}", sendAccessService.privateAccessCheck("111", "2222"));
    }
}