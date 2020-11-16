package com.gylang.netty.sdk;

/**
 * im服务上下文实现类
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public class DefaultImApplicationContext extends SimpleImFactory implements ImApplicationContext {


    @Override
    public void sendMsg(String key, Object message) {
        getMessagePusher().sendMsg(key, message);
    }

    @Override
    public void sendAsyncMsg(String key, Object message) {
        getMessagePusher().sendAsyncMsg(key, message);
    }

    public void doInit(ImFactoryBuilder builder) {

        // 初始化参数
        setMessagePusher(builder.getNotifyProvider());
        setNettyConfig(builder.getNettyConfig());
        setRequestAdapter(builder.getDispatchAdapter());
        // 初始化netty服务
        super.init();


    }
}
