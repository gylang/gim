package com.gylang.gim.web.service.biz;

import com.gylang.gim.web.api.domain.common.CommonResult;
import com.gylang.gim.web.api.dto.request.LoginRequest;
import com.gylang.gim.web.api.dto.request.RegistryRequest;
import com.gylang.gim.web.api.dto.response.LoginResponse;
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
