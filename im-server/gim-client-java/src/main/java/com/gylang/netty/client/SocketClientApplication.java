package com.gylang.netty.client;

import com.alibaba.fastjson.JSON;
import com.gylang.netty.client.api.AuthApi;
import com.gylang.netty.client.call.ICallback;
import com.gylang.netty.client.domain.CommonResult;
import com.gylang.netty.client.domain.request.LoginRequest;
import com.gylang.netty.client.domain.response.LoginResponse;
import com.gylang.netty.client.util.HttpUtil;
import com.gylang.netty.client.woker.SocketHolder;
import com.gylang.netty.client.woker.SocketManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author gylang
 * data 2021/3/31
 */
public class SocketClientApplication {


    public static void run() {

        // 连接socket
        SocketManager socketManager = SocketHolder.getInstance();
        socketManager.connect("127.0.0.1", 46001, str -> {
            if ("1".equals(str)) {
                System.out.println("连接成功！");
            } else {
                System.out.println(str);
            }
        });

        // 初始化 retrofit
        HttpUtil.init();

        AuthApi authApi = HttpUtil.getApi(AuthApi.class);
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");
        Call<CommonResult<LoginResponse>> resultCall = authApi.login(request);
        resultCall.enqueue(new ICallback<CommonResult<LoginResponse>>() {
            @Override
            public void success(Call<CommonResult<LoginResponse>> call, Response<CommonResult<LoginResponse>> response) {
                CommonResult<LoginResponse> body = response.body();
                LoginResponse data = body.getData();
                System.out.println("[登录成功] : " + data.getUsername());
                System.out.println("[登录成功] : " + data.getToken());
                data.setUsername("111");
                System.out.println(data.getUsername());
            }

            @Override
            public void fail(Call<CommonResult<LoginResponse>> call, CommonResult<?> response) {

            }
        });

    }


    public static void main(String[] args) {
        Thread thread = new Thread(() -> run());
        thread.setDaemon(false);
        thread.start();
    }
}
