package com.gylang.spring.netty;

import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.DefaultMessageProvider;
import com.gylang.netty.sdk.MessageProvider;
import com.gylang.netty.sdk.call.DefaultNotifyProvider;
import com.gylang.netty.sdk.call.NotifyContext;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.call.message.MessageNotifyListener;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.handler.NettyController;
import com.gylang.netty.sdk.handler.adapter.DefaultNettyControllerAdapter;
import com.gylang.netty.sdk.handler.adapter.DefaultRequestHandlerAdapter;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import com.gylang.spring.netty.custom.adapter.MethodArgumentResolverAdapter;
import com.gylang.spring.netty.register.PrepareConfiguration;
import io.netty.channel.ChannelInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2020/11/30
 */
@Configuration
@AutoConfigureBefore(PrepareConfiguration.class)
@AutoConfigureAfter(InitNettyServerConfiguration.class)
@Import({PrepareConfiguration.class, InitNettyServerConfiguration.class})
@ComponentScan("com.gylang.spring.netty.custom")
@Slf4j
public class NettyAutoConfiguration implements InitializingBean {

    /**
     * 消息监听器
     */
    @Autowired(required = false)
    private List<MessageNotifyListener> messageNotifyListenerList;

    @Autowired(required = false)
    private List<IMRequestHandler> requestHandlerList;

    @Autowired(required = false)
    private List<NettyController<?>> nettyControllerList;

    @Autowired(required = false)
    private NettyUserInfoFillHandler nettyUserInfoFillHandler;

    @Resource
    private IMSessionRepository sessionRepository;
    @Resource
    private IMGroupSessionRepository groupSessionRepository;
    @Resource
    private ThreadPoolExecutor imExecutor;
    @Resource
    private DataConverter dataConverter;
    @Resource
    private NotifyContext notifyContext;


    @Resource
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean(MessageProvider.class)
    public MessageProvider messageProvider() {

        return new DefaultMessageProvider(sessionRepository, groupSessionRepository, imExecutor, dataConverter);
    }

    @Bean
    @ConditionalOnMissingBean(NotifyProvider.class)
    public NotifyProvider notifyProvider() {
        DefaultNotifyProvider defaultNotifyProvider = new DefaultNotifyProvider();
        defaultNotifyProvider.setExecutor(imExecutor);
        defaultNotifyProvider.setNotifyContext(notifyContext);
        return defaultNotifyProvider;
    }

    @Bean
    public BizRequestAdapter nettyControllerAdapter() {

        DefaultNettyControllerAdapter nettyControllerAdapter = new DefaultNettyControllerAdapter();
        nettyControllerAdapter.register(nettyControllerList);
        return nettyControllerAdapter;
    }

    @Bean
    public BizRequestAdapter nettyHandlerAdapter() {

        DefaultRequestHandlerAdapter requestHandlerAdapter = new DefaultRequestHandlerAdapter();
        requestHandlerAdapter.register(requestHandlerList);
        return requestHandlerAdapter;
    }



    @Override
    public void afterPropertiesSet() throws Exception {

        // 初始化消息监听


        log.info("初始化基础配置完成");
    }
}
