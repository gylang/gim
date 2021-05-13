package com.gylang.gim.admin.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;

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


    public void initConfig(NettyConfiguration configuration) {

        // 反射指定执行 AfterConfigInitialize init
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


    }

    private void judgeInvokeAfterInitialize(Object value, NettyConfiguration configuration) {

        if (value instanceof AfterConfigInitialize) {
            ((AfterConfigInitialize) value).init(configuration);
        }
    }
}
