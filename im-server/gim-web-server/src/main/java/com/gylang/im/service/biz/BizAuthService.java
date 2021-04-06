package com.gylang.im.service.biz;

import com.gylang.im.api.domain.common.CommonResult;
import com.gylang.im.api.dto.request.LoginRequest;
import com.gylang.im.api.dto.request.RegistryRequest;
import com.gylang.im.api.dto.response.LoginResponse;
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
