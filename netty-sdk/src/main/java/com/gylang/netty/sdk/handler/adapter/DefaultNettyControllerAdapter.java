package com.gylang.netty.sdk.handler.adapter;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.annotation.AdapterType;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.NettyController;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
@AdapterType(order = 100)
public class DefaultNettyControllerAdapter implements BizRequestAdapter {
    private static String METHOD_NAME = "process";
    private Map<String, NettyController<?>> nettyControllerMap;
    private Map<String, Class<?>> paramTypeMap;
    @Setter
    private DataConverter dataConverter;

    @Override
    public void process(ChannelHandlerContext ctx, IMSession me, MessageWrap message, NotifyProvider messagePusher) {

        NettyController<?> nettyController = nettyControllerMap.get(message.getKey());
        Class<?> paramType = paramTypeMap.get(message.getKey());
        if (null == nettyController || null == paramType) {
            return;
        }

        ((NettyController<Object>) nettyController).process(me, dataConverter.converterTo(paramType, message));
    }

    @Override
    public void register(List<?> nettyControllerList) {

        if (null == nettyControllerList) {
            nettyControllerMap = CollUtil.newHashMap();
            paramTypeMap = CollUtil.newHashMap();
            return;
        }
        nettyControllerMap = CollUtil.newHashMap(nettyControllerList.size());
        paramTypeMap = CollUtil.newHashMap(nettyControllerList.size());
        List<NettyController<?>> nettyControllers = getTargetList(nettyControllerList);
        for (NettyController<?> nettyController : nettyControllers) {

            Class<?> clazz = nettyController.getClass();
            NettyHandler nettyHandler = clazz.getAnnotation(NettyHandler.class);
            if (null != nettyHandler) {

                for (Method method : clazz.getDeclaredMethods()) {
                    if (METHOD_NAME.equals(method.getName()) && !method.isBridge()) {
                        nettyControllerMap.put(nettyHandler.value(), nettyController);
                        paramTypeMap.put(nettyHandler.value(), method.getParameterTypes()[1]);
                    }
                }

            }
        }
    }

    @Override
    public List<?> mappingList() {
        return new ArrayList<>(nettyControllerMap.values());
    }
}
