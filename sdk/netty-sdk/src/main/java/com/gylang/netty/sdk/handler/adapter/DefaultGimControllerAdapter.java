package com.gylang.netty.sdk.handler.adapter;

import cn.hutool.core.collection.CollUtil;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.common.InvokeFinished;
import com.gylang.netty.sdk.api.common.ObjectWrap;
import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.api.conveter.DataConverter;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.BizRequestAdapter;
import com.gylang.netty.sdk.api.handler.GimController;
import com.gylang.netty.sdk.api.util.ObjectWrapUtil;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * netty分发处理器 分配到指定的业务 NettyController
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 * @see GimController
 */
public class DefaultGimControllerAdapter implements BizRequestAdapter {

    private static final String METHOD_NAME = "process";
    private Map<Integer, GimController<?>> nettyControllerMap;
    private Map<Integer, Class<?>> paramTypeMap;
    private DataConverter dataConverter;


    @Override
    public Object process(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {

        if (null != nettyControllerMap && null != paramTypeMap) {

            GimController<?> gimController = nettyControllerMap.get(message.getType());
            Class<?> paramType = paramTypeMap.get(message.getType());
            if (null == gimController || null == paramType) {
                return null;
            }
            @SuppressWarnings("unchecked")
            Object result = ((GimController<Object>) gimController)
                    .process(me, dataConverter.converterTo(paramType, message));
            return InvokeFinished.finish(result);
        }
        return null;
    }

    @Override
    public void register(List<ObjectWrap> nettyControllerList) {
        // 判断注册列表是否为空
        if (null == nettyControllerList) {
            nettyControllerMap = CollUtil.newHashMap();
            paramTypeMap = CollUtil.newHashMap();
            return;
        }
        nettyControllerMap = CollUtil.newHashMap(nettyControllerList.size());
        paramTypeMap = CollUtil.newHashMap(nettyControllerList.size());
        for (ObjectWrap objectWrap : nettyControllerList) {

            Object instance = objectWrap.getInstance();
            if (instance instanceof GimController) {
                NettyHandler nettyHandler = ObjectWrapUtil.findAnnotation(NettyHandler.class, objectWrap);
                if (null != nettyHandler) {
                    for (Method method : objectWrap.getUserType().getDeclaredMethods()) {
                        if (METHOD_NAME.equals(method.getName()) && !method.isBridge()) {
                            nettyControllerMap.put(nettyHandler.value(), (GimController<?>) instance);
                            paramTypeMap.put(nettyHandler.value(), method.getParameterTypes()[1]);
                        }
                    }

                }
            }
        }
    }

    @Override
    public Integer order() {
        return null;
    }

    @Override
    public void init(GimGlobalConfiguration gimGlobalConfiguration) {
        dataConverter = gimGlobalConfiguration.getDataConverter();
        register(gimGlobalConfiguration.getObjectWrapList());
    }


}
