package com.gylang.netty.sdk.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gylang.netty.sdk.MethodMeta;
import com.gylang.netty.sdk.annotation.NettyController;
import com.gylang.netty.sdk.annotation.NettyMapping;
import com.gylang.netty.sdk.call.MessagePusher;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于方法的实现进行业务处理分发
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
public class SimpleMethodHandlerAdapter implements IMRequestAdapter {

    private Map<String, MethodMeta> methodMetaMap;

    private DataConverter dataConverter;

    @Override
    public void process(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MessagePusher messagePusher) {

    }

    public void register(List<Object> controllerList) {

        if (null == controllerList) {
            return;
        }
        methodMetaMap = new HashMap<>();
        for (Object o : controllerList) {

            Class<?> clazz = o.getClass();
            NettyController controller = clazz.getAnnotation(NettyController.class);
            String baseKey = null == controller || StrUtil.isBlank(controller.value()) ? null : controller.value();
            for (Method method : clazz.getDeclaredMethods()) {
                NettyMapping nettyMapping = method.getAnnotation(NettyMapping.class);
                if (null != nettyMapping) {

                    for (Class<?> parameterType : method.getParameterTypes()) {

                    }
                    String mappingKey = ArrayUtil.join(new String[]{baseKey, nettyMapping.value()}, "/");
                    MethodMeta methodMeta = new MethodMeta();
                    methodMeta.setMethod(method);
                    methodMeta.setObject(controller);
                    methodMetaMap.put(mappingKey, methodMeta);
                }
            }
        }
    }


}
