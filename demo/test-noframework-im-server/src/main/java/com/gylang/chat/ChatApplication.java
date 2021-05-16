package com.gylang.chat;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.IMServer;
import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.api.config.DefaultGimConfigurationInitializerHelper;
import com.gylang.netty.sdk.api.util.ObjectWrapUtil;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
public class ChatApplication {

    public static void main(String[] args) throws InterruptedException, IllegalAccessException {

        // 启动服务
        NettyConfigHolder.init();
        //业务执行参数
        GimGlobalConfiguration gimGlobalConfiguration = NettyConfigHolder.getInstance();
        gimGlobalConfiguration.addObjectWrap(ObjectWrapUtil.resolver(JoinGroupHandler.class, new JoinGroupHandler()));
        gimGlobalConfiguration.addObjectWrap(ObjectWrapUtil.resolver(SimpleChatGroupHandler.class, new SimpleChatGroupHandler()));
        gimGlobalConfiguration.setMessageEventListener(CollUtil.newArrayList(new TestEventListener()));
        new DefaultGimConfigurationInitializerHelper().initConfig(gimGlobalConfiguration);
        IMServer imServer = new IMServer();
        imServer.setNettyConfig(gimGlobalConfiguration);
        imServer.start();
    }
}
