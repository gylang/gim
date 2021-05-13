package com.gylang.gim.client.api;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.request.LoginRequest;
import com.gylang.gim.api.domain.request.RegistryRequest;
import com.gylang.gim.api.dto.response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author gylang
 * data 2021/3/31
 */
public interface AuthApi {

    String AUTH_BASE = "/api/auth";

    /**
     * 用户登录
     *
     * @param request 请求 参数
     * @return 登录结果
     */
    @POST(AUTH_BASE + "/login")
    Call<CommonResult<LoginResponse>> login(@Body LoginRequest request);


    /**
     * 用户注册
     *
     * @param request 请求 参数
     * @return 登录结果
     */
    @POST(AUTH_BASE + "/registry")
    Call<CommonResult<LoginResponse>> registry(@Body RegistryRequest request);
}
