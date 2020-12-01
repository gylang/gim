package com.gylang.spring.netty.register;

import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.call.DefaultNotifyContext;
import com.gylang.netty.sdk.call.DefaultNotifyProvider;
import com.gylang.netty.sdk.call.NotifyContext;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.handler.DefaultAdapterDispatch;
import com.gylang.netty.sdk.handler.DispatchAdapterHandler;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gylang
 * data 2020/12/1
 */
@Configuration
public class PrepareConfiguration {

    @Value("${im.converterType:com.gylang.netty.sdk.conveter.JsonConverter}")
    private String converterType;

    @Bean
    @ConditionalOnMissingBean(DispatchAdapterHandler.class)
    public DispatchAdapterHandler dispatchAdapterHandler() {

        DefaultAdapterDispatch defaultAdapterDispatch = new DefaultAdapterDispatch();
        return defaultAdapterDispatch;
    }


    @Bean
    @ConditionalOnMissingBean(NotifyContext.class)
    public NotifyContext notifyContext() {
        return new DefaultNotifyContext();
    }

    @Bean
    @ConditionalOnMissingBean(NettyConfig.class)
    public NettyConfig nettyConfig() {
        return new NettyConfig();
    }

    @Bean
    @ConditionalOnMissingBean(IMSessionRepository.class)
    public IMSessionRepository imSessionRepository() {
        return new DefaultIMRepository();
    }

    @Bean
    @ConditionalOnMissingBean(IMGroupSessionRepository.class)
    public IMGroupSessionRepository imGroupSessionRepository() {

        return new DefaultGroupRepository();
    }

    @Bean
    @ConditionalOnMissingBean(DataConverter.class)
    public DataConverter dataConverter() {

        return ReflectUtil.newInstance(this.converterType);
    }


    @Bean
    public ThreadPoolExecutor imExecutor() {
        return new ThreadPoolExecutor(50, 92, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new DefaultThreadFactory("im线程数池"));
    }

}
