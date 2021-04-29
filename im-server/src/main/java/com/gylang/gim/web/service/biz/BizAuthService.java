package com.gylang.gim.web.service.biz;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.web.dto.request.LoginRequest;
import com.gylang.gim.web.dto.request.RegistryRequest;
import com.gylang.gim.web.dto.response.LoginResponse;
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
