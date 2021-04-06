package com.gylang.im.controller;

import com.gylang.cache.CacheManager;
import com.gylang.im.api.domain.common.CommonResult;
import com.gylang.im.domain.UserCache;
import com.gylang.im.api.dto.request.LoginRequest;
import com.gylang.im.api.dto.request.RegistryRequest;
import com.gylang.im.api.dto.response.LoginResponse;
import com.gylang.im.service.biz.BizAuthService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/3
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Resource
    private BizAuthService bizAuthService;
    @Resource
    private CacheManager cacheManager;

    @RequestMapping("login")
    public CommonResult<LoginResponse> login(@RequestBody LoginRequest request) {

        return bizAuthService.login(request);
    }

    @RequestMapping("registry")
    @Transactional
    public CommonResult<Boolean> registry(@RequestBody RegistryRequest request) {

        return bizAuthService.registry(request);
    }


    @GetMapping("userInfo")
    public CommonResult<UserCache> getUserInfo(@RequestHeader("token") String token) {
        // 判断是否存在 状态是否正常
        return CommonResult.fail(cacheManager.get(token));
    }

}
