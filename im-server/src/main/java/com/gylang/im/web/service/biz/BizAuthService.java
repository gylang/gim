package com.gylang.im.web.service.biz;

import com.gylang.im.web.dto.request.LoginRequest;
import com.gylang.im.web.dto.request.RegistryRequest;
import com.gylang.im.web.dto.response.LoginResponse;
import com.gylang.im.common.dto.CommonResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author gylang
 * data 2021/3/6
 */
public interface BizAuthService {


    CommonResult<LoginResponse> login(@RequestBody LoginRequest request);

    @Transactional
    CommonResult<Boolean> registry(RegistryRequest request);
}
