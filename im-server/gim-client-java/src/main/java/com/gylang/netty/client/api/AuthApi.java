package com.gylang.netty.client.api;

import com.gylang.netty.client.domain.CommonResult;
import com.gylang.netty.client.domain.request.LoginRequest;
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
    Call<CommonResult<String>> login(@Body LoginRequest request);
}
