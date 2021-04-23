package com.gylang.netty.sdk.handler.adapter;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.common.InokeFinished;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.NettyController;
import com.gylang.netty.sdk.util.ObjectWrapUtil;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * netty分发处理器 分配到指定的业务 NettyController
 *
 * @author gylang
 * data 2020/11/9
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 * @see com.gylang.netty.sdk.handler.NettyController
 */
public class DefaultNettyControllerAdapter implements BizRequestAdapter<NettyController<?>> {

    private static final String METHOD_NAME = "process";
    private Map<Integer, NettyController<?>> nettyControllerMap;
    private Map<Integer, Class<?>> paramTypeMap;
    private DataConverter dataConverter;


    @Override
    public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {

        if (null != nettyControllerMap && null != paramTypeMap) {

            NettyController<?> nettyController = nettyControllerMap.get(message.getType());
            Class<?> paramType = paramTypeMap.get(message.getType());
            if (null == nettyController || null == paramType) {
                return null;
            }

            Object result = ((NettyController<Object>) nettyController)
                    .process(me, dataConverter.converterTo(paramType, message));
            return InokeFinished.finish(result);
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
            if (instance instanceof NettyController) {
                NettyHandler nettyHandler = ObjectWrapUtil.findAnnotation(NettyHandler.class, objectWrap);
                if (null != nettyHandler) {
                    for (Method method : objectWrap.getUserType().getDeclaredMethods()) {
                        if (METHOD_NAME.equals(method.getName()) && !method.isBridge()) {
                            nettyControllerMap.put(nettyHandler.value(), (NettyController<?>) instance);
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
    public void init(NettyConfiguration nettyConfiguration) {
        dataConverter = nettyConfiguration.getDataConverter();
        register(nettyConfiguration.getObjectWrapList());
    }


}
