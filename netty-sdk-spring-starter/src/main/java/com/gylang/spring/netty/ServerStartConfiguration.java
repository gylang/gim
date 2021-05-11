package com.gylang.spring.netty;

import com.gylang.netty.sdk.IMServer;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.config.GimProperties;
import com.gylang.netty.sdk.config.DefaultGimConfigurationInitializerHelper;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.IMessageRouter;
import com.gylang.netty.sdk.handler.qos.IMessageReceiveQosHandler;
import com.gylang.netty.sdk.handler.qos.IMessageSenderQosHandler;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import com.gylang.netty.sdk.intercept.NettyIntercept;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2020/12/1
 */
@Configuration
@Slf4j
public class ServerStartConfiguration implements InitializingBean {

    /**
     * 根据名称装配，防止和客户端的ChannelInitializer冲突报错
     */
    @Resource
    private List<CustomInitializer<?>> serverChannelInitializer;
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
    private List<BizRequestAdapter> bizRequestAdapterList;
    /** 适配分发器 */
    @Resource
    private IMessageRouter messageRouter;
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
    private IMessageSenderQosHandler iMessageSenderQosHandler;
    @Resource
    private IMessageReceiveQosHandler iMessageReceiveQosHandler;
    @Resource
    private GimGlobalConfiguration gimGlobalConfiguration;
    @Resource
    private GimProperties gimProperties;

    @Override
    public void afterPropertiesSet() throws Exception {

        gimGlobalConfiguration.setServerChannelInitializer(serverChannelInitializer);
        gimGlobalConfiguration.setEventProvider(eventProvider);
        gimGlobalConfiguration.setEventContext(eventContext);
        gimGlobalConfiguration.setDataConverter(dataConverter);
        gimGlobalConfiguration.setSessionRepository(sessionRepository);
        gimGlobalConfiguration.setGroupSessionRepository(groupSessionRepository);
        gimGlobalConfiguration.setMessageProvider(messageProvider);
        gimGlobalConfiguration.setMessageEventListener(messageEventListener);
        gimGlobalConfiguration.setBizRequestAdapterList(bizRequestAdapterList);
        gimGlobalConfiguration.setIMessageRouter(messageRouter);
        gimGlobalConfiguration.setPoolExecutor(poolExecutor);
        gimGlobalConfiguration.setNettyUserInfoFillHandler(nettyUserInfoFillHandler);
        gimGlobalConfiguration.setNettyInterceptList(nettyInterceptList);
        gimGlobalConfiguration.setIMessageReceiveQosHandler(iMessageReceiveQosHandler);
        gimGlobalConfiguration.setIMessageSenderQosHandler(iMessageSenderQosHandler);
        gimGlobalConfiguration.setGimProperties(gimProperties);
        new DefaultGimConfigurationInitializerHelper().initConfig(gimGlobalConfiguration);

        // 启动服务
        IMServer imServer = new IMServer();
        imServer.setNettyConfig(gimGlobalConfiguration);
        imServer.start();
        log.info("初始化基础配置完成 : InitNettyServerConfiguration");

    }
}
