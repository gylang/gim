package com.gylang.netty.sdk.api.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.sdk.api.common.AfterConfigInitialize;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * netty全局配置配置初始化辅助器 帮助初始化各种gim配置
 *
 * @author gylang
 * data 2021/3/2
 */
public class DefaultGimConfigurationInitializerHelper {


    @SuppressWarnings("unchecked")
    public void initConfig(GimGlobalConfiguration configuration) {

        // 执行所有包含AfterConfigInitialize的init方法
        Field[] fields = ReflectUtil.getFields(configuration.getClass());
        for (Field field : fields) {
            // 集合判断
            if (ClassUtil.isAssignable(Map.class, field.getType())) {
                Map<Object, Object> map = (Map<Object, Object>) ReflectUtil.getFieldValue(configuration, field);
                if (CollUtil.isNotEmpty(map)) {
                    for (Object o : map.values()) {
                        judgeInvokeAfterInitialize(o, configuration);
                    }
                }
            } else if (ClassUtil.isAssignable(Collection.class, field.getType())) {
                List<Object> list = (List<Object>) ReflectUtil.getFieldValue(configuration, field);
                if (CollUtil.isNotEmpty(list)) {
                    for (Object o : list) {
                        judgeInvokeAfterInitialize(o, configuration);
                    }
                }
            } else if (ClassUtil.isAssignable(AfterConfigInitialize.class, field.getType())) {
                // 非集合直接调用
                judgeInvokeAfterInitialize(ReflectUtil.getFieldValue(configuration, field), configuration);
            }
        }

        configuration.getIMessageSenderQosHandler().startup();

        configuration.getIMessageReceiveQosHandler().startup();

    }

    /**
     * 调用 初始化方法
     *
     * @param afterInitializer 初始化接口
     * @param configuration    全局配置类
     */
    private void judgeInvokeAfterInitialize(Object afterInitializer, GimGlobalConfiguration configuration) {

        if (afterInitializer instanceof AfterConfigInitialize) {
            ((AfterConfigInitialize) afterInitializer).init(configuration);
        }
    }
}
