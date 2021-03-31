package com.gylang.netty.client.util;

import cn.hutool.core.lang.ClassScanner;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import com.gylang.netty.client.interceptor.CommonInterecptor;
import com.gylang.netty.client.interceptor.JsonInterecptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import sun.net.www.protocol.http.logging.HttpLogFormatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
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

        OkHttpClient.Builder jsonClient = new OkHttpClient.Builder()
                //设置超时时间
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new CommonInterecptor())
                .addInterceptor(new JsonInterecptor())
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
