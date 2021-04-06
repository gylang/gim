package com.gylang.im;

import cn.hutool.core.util.RandomUtil;
import com.gylang.im.api.dto.request.RegistryRequest;
import com.gylang.im.service.biz.BizAuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/8
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DataTest {

    @Resource
    private BizAuthService bizAuthService;

    @Test
    public void userData() {

        for (int i = 0; i < 1000000; i++) {

            RegistryRequest request = new RegistryRequest();
            request.setUsername(RandomUtil.randomNumbers(16));
            request.setPassword("123456");
            request.setEmail(RandomUtil.randomString(8) + "@" + RandomUtil.randomString(6) + ".com");
            request.setTel("1" + RandomUtil.randomNumbers(10));
            bizAuthService.registry(request);
        }

    }
}
