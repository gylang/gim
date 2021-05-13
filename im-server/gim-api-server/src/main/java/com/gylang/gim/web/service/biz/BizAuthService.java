package com.gylang.gim.web.service.biz;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.entity.UserCache;
import com.gylang.gim.api.dto.request.LoginRequest;
import com.gylang.gim.api.dto.request.RegistryRequest;
import com.gylang.gim.api.dto.response.LoginResponse;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author gylang
 * data 2021/3/6
 */
public interface BizAuthService {


    /**
     * 登录
     *
     * @param request
     * @return
     */
    CommonResult<LoginResponse> login(@RequestBody LoginRequest request);

    /**
     * 注册
     *
     * @param request
     * @return
     */
    CommonResult<Boolean> registry(RegistryRequest request);

    /**
     * 通知im服务
     *
     * @param userCache
     */
    void pushNotify2ImServer(LoginResponse userCache);
}
