package com.gylang.chat;

import com.gylang.netty.sdk.IMServer;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.config.SimpleNettyConfigurationInitializer;
import com.gylang.netty.sdk.util.ObjectWrapUtil;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
public class ChatApplication {

    public static void main(String[] args) throws InterruptedException {

        // 启动服务
        NettyConfiguration nettyConfiguration = new TestStartConfig().init();
        //业务执行参数
        nettyConfiguration.addObjectWrap(ObjectWrapUtil.resolver(JoinGroupHandler.class, new JoinGroupHandler()));
        nettyConfiguration.addObjectWrap(ObjectWrapUtil.resolver(SimpleChatGroupHandler.class, new SimpleChatGroupHandler()));

        new SimpleNettyConfigurationInitializer().initConfig(nettyConfiguration);
        IMServer imServer = new IMServer();
        imServer.setNettyConfig(nettyConfiguration);
        imServer.start();
    }
}
