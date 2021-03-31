package com.gylang.netty.client.interceptor;

import com.gylang.netty.client.util.Store.UserStore;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author gylang
 * data 2021/3/31
 */
public class TokenInterecptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request().newBuilder()
                .addHeader("token", UserStore.getInstance().getToken())
                .build();
        return chain.proceed(request);
    }
}
