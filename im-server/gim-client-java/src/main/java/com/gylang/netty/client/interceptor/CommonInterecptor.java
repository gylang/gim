package com.gylang.netty.client.interceptor;

import cn.hutool.core.util.StrUtil;
import com.gylang.netty.client.util.Store.UserStore;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author gylang
 * data 2021/3/31
 */
public class CommonInterecptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {


        String token = UserStore.getInstance().getToken();
        Request request = chain.request();
        if (StrUtil.isNotEmpty(token)) {
            request = request.newBuilder()
                    .addHeader("token", token)
                    .build();
        }

        return chain.proceed(request);
    }
}
