package com.gylang.spring.netty;

import com.gylang.netty.sdk.IMServer;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.config.SimpleNettyConfigurationInitializer;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.DispatchAdapterHandler;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import com.gylang.netty.sdk.intercept.NettyIntercept;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2020/12/1
 */
@Configuration
@AutoConfigureBefore(StartNettyServer.class)
@Slf4j
public class InitNettyServerConfiguration implements InitializingBean {

    /**
     * 根据名称装配，防止和客户端的ChannelInitializer冲突报错
     */
    @Resource
    private CustomInitializer<?> serverChannelInitializer;
    /** 事件监听 */
    @Resource
    private EventProvider eventProvider;
    /** 时间上下文 */
    /** 时间上下文 */
    @Resource
    private EventContext eventContext;
    /** 数据类型转化 */
    @Resource
    private DataConverter dataConverter;
    /** 单用户会话工厂 */
    @Resource
    private IMSessionRepository sessionRepository;
    /** 用户组会话工厂 */
    @Resource
    private IMGroupSessionRepository groupSessionRepository;
    /** 消息发送provider */
    @Resource
    private MessageProvider messageProvider;
    /** 事件监听列表 */
    @Autowired(required = false)
    private List<MessageEventListener<?>> messageEventListener;
    /** 业务请求适配器 */
    @Resource
    private List<BizRequestAdapter<?>> bizRequestAdapterList;
    /** 适配分发器 */
    @Resource
    private DispatchAdapterHandler dispatchAdapterHandler;
    /** 线程池 */
    @Resource
    private ThreadPoolExecutor poolExecutor;
    /** 填充客户信息 */
    @Autowired(required = false)
    private NettyUserInfoFillHandler nettyUserInfoFillHandler;
    /** 消息处理拦截器 */
    @Autowired(required = false)
    private List<NettyIntercept> nettyInterceptList;
    @Resource
    private NettyConfiguration nettyConfiguration;


    @Override
    public void afterPropertiesSet() throws Exception {

        nettyConfiguration.setServerChannelInitializer(serverChannelInitializer);
        nettyConfiguration.setEventProvider(eventProvider);
        nettyConfiguration.setEventContext(eventContext);
        nettyConfiguration.setDataConverter(dataConverter);
        nettyConfiguration.setSessionRepository(sessionRepository);
        nettyConfiguration.setGroupSessionRepository(groupSessionRepository);
        nettyConfiguration.setMessageProvider(messageProvider);
        nettyConfiguration.setMessageEventListener(messageEventListener);
        nettyConfiguration.setBizRequestAdapterList(bizRequestAdapterList);
        nettyConfiguration.setDispatchAdapterHandler(dispatchAdapterHandler);
        nettyConfiguration.setPoolExecutor(poolExecutor);
        nettyConfiguration.setNettyUserInfoFillHandler(nettyUserInfoFillHandler);
        nettyConfiguration.setNettyInterceptList(nettyInterceptList);
        new SimpleNettyConfigurationInitializer().initConfig(nettyConfiguration);

        // 启动服务
        IMServer imServer = new IMServer();
        imServer.setNettyConfig(nettyConfiguration);
        imServer.start();
        log.info("初始化基础配置完成 : InitNettyServerConfiguration");

    }
}
