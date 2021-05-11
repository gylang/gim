package com.gylang.netty.sdk.handler.adapter;

import cn.hutool.core.collection.CollUtil;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.common.InvokeFinished;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.util.ObjectWrapUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

/**
 * netty分发处理器 分配到指定的业务 IMRequestHandler
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 * @see com.gylang.netty.sdk.handler.IMRequestHandler
 */
public class DefaultRequestHandlerAdapter implements BizRequestAdapter {


    private Map<Integer, IMRequestHandler> handlerMap;

    @Override
    public Object process(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {


        IMRequestHandler requestHandler = handlerMap.get(message.getType());
        if (null == requestHandler) {
            return null;
        }
        Object result = requestHandler.process(me, message);
        return InvokeFinished.finish(result);
    }

    @Override
    public void register(List<ObjectWrap> requestHandlerList) {
        if (null == requestHandlerList) {
            handlerMap = CollUtil.newHashMap();
            return;
        }
        handlerMap = CollUtil.newHashMap(requestHandlerList.size());
        for (ObjectWrap objectWrap : requestHandlerList) {
            if (objectWrap.getInstance() instanceof IMRequestHandler) {
                NettyHandler annotation = ObjectWrapUtil.findAnnotation(NettyHandler.class, objectWrap);
                if (null != annotation) {
                    handlerMap.put(annotation.value(), (IMRequestHandler) objectWrap.getInstance());
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
        register(gimGlobalConfiguration.getObjectWrapList());
    }


}
