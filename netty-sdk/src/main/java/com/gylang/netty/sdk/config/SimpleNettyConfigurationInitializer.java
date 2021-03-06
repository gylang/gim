package com.gylang.netty.sdk.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.common.AfterConfigInitialize;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 初始化配置
 *
 * @author gylang
 * data 2021/3/2
 */
public class SimpleNettyConfigurationInitializer {


    public void initConfig(NettyConfiguration configuration) throws IllegalAccessException {

//        // 事件上下文
//        EventContext eventContext = configuration.getEventContext();
//        eventContext.init(configuration);
//        // 消息发送
//        configuration.getEventProvider().init(configuration);
//        // 消息发送器
//        configuration.getMessageProvider().init(configuration);
//        // 消息业务适配器
//        for (BizRequestAdapter<?> bizRequestAdapter : configuration.getBizRequestAdapterList()) {
//            bizRequestAdapter.init(configuration);
//        }
//        CustomAfterConfigInitialize<?> serverChannelInitializer = configuration.getServerChannelInitializer();
//        serverChannelInitializer.init(configuration);
//        // 消息分发
//        configuration.getDispatchAdapterHandler().init(configuration);
//        // qos
//        IMessageReceiveQosHandler iMessageReceiveQosHandler = configuration.getIMessageReceiveQosHandler();
//        iMessageReceiveQosHandler.init(configuration);
//        iMessageReceiveQosHandler.startup();
//        IMessageSenderQosHandler iMessageSenderQosHandler = configuration.getIMessageSenderQosHandler();
//        iMessageSenderQosHandler.init(configuration);
//        iMessageSenderQosHandler.startup();

        // 所有包含Initializer的都执行下init方法
        Field[] fields = ReflectUtil.getFields(configuration.getClass());
        for (Field field : fields) {

            if (ClassUtil.isAssignable(Map.class, field.getType())) {
                Map map = (Map) ReflectUtil.getFieldValue(configuration, field);
                if (CollUtil.isNotEmpty(map)) {
                    for (Object o : map.values()) {
                        judgeInvokeAfterInitialize(o, configuration);
                    }
                }
            } else if (ClassUtil.isAssignable(Collection.class, field.getType())) {
                List list = (List) ReflectUtil.getFieldValue(configuration, field);
                if (CollUtil.isNotEmpty(list)) {
                    for (Object o : list) {
                        judgeInvokeAfterInitialize(o, configuration);
                    }
                }
            } else if (ClassUtil.isAssignable(AfterConfigInitialize.class, field.getType())) {

                judgeInvokeAfterInitialize(ReflectUtil.getFieldValue(configuration, field), configuration);
            }
        }

        configuration.getIMessageSenderQosHandler().startup();

        configuration.getIMessageReceiveQosHandler().startup();

    }

    private void judgeInvokeAfterInitialize(Object value, NettyConfiguration configuration) {

        if (value instanceof AfterConfigInitialize) {
            ((AfterConfigInitialize) value).init(configuration);
        }
    }
}
