package com.gylang.netty.client.interceptor;

import okhttp3.*;

import java.io.IOException;

/**
 * @author gylang
 * data 2021/3/31
 */
public class JsonInterecptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("contentType", "application/json")
                .build();
        return chain.proceed(request);

    }
}
