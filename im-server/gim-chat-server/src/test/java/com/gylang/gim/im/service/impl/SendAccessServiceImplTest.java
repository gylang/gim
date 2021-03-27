package com.gylang.gim.im.service.impl;

import com.gylang.gim.im.BaseTest;
import com.gylang.gim.im.service.SendAccessService;
import junit.runner.BaseTestRunner;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

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