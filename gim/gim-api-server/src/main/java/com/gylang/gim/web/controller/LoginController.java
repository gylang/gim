package com.gylang.gim.web.controller;

import com.gylang.cache.CacheManager;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.entity.UserCache;
import com.gylang.gim.api.dto.request.LoginRequest;
import com.gylang.gim.api.dto.request.RegistryRequest;
import com.gylang.gim.api.dto.response.LoginResponse;
import com.gylang.gim.web.service.biz.BizAuthService;
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

    /**
     * 登录
     *
     * @param request
     * @return
     */
    @RequestMapping("login")
    public CommonResult<LoginResponse> login(@RequestBody LoginRequest request) {

        return bizAuthService.login(request);
    }

    /**
     * 注册
     *
     * @param request
     * @return
     */
    @RequestMapping("registry")
    public CommonResult<Boolean> registry(@RequestBody RegistryRequest request) {

        return bizAuthService.registry(request);
    }


    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("userInfo")
    public CommonResult<UserCache> getUserInfo(@RequestHeader("token") String token) {
        // 判断是否存在 状态是否正常
        return CommonResult.ok(cacheManager.get(token));
    }

}
