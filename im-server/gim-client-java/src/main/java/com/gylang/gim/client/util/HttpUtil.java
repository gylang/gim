package com.gylang.gim.client.util;

import cn.hutool.core.lang.ClassScanner;
import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import com.gylang.gim.client.interceptor.CommonInterecptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author gylang
 * data 2021/3/31
 */
public class HttpUtil {

    private static final Map<Class<?>, Object> apiProxy = new HashMap<>();
    private static long DEFAULT_TIMEOUT = 30L;

    public static void init() {

        Set<Class<?>> apiClazzList = ClassScanner.scanPackage("com.gylang.netty.client.api");



        OkHttpClient.Builder client = new OkHttpClient.Builder()
                //设置超时时间
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new CommonInterecptor())
                .retryOnConnectionFailure(true);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http:127.0.0.1:8888")
                .client(client.build())
                .addConverterFactory(Retrofit2ConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        // 构建api代理了
        for (Class<?> clazz : apiClazzList) {

            apiProxy.put(clazz, retrofit.create(clazz));
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> T getApi(Class<T> clazz) {

        return (T) apiProxy.get(clazz);
    }
}
