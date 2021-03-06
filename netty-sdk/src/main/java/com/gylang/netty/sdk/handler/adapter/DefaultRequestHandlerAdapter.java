package com.gylang.netty.sdk.handler.adapter;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.common.InokeFinished;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
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
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 * @see com.gylang.netty.sdk.handler.IMRequestHandler
 */
public class DefaultRequestHandlerAdapter implements BizRequestAdapter<IMRequestHandler> {


    private Map<String, IMRequestHandler> handlerMap;

    @Override
    public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {

        IMRequestHandler requestHandler = handlerMap.get(message.getCmd());
        if (null == requestHandler) {
            return null;
        }
        Object result = requestHandler.process(me, message);
        return InokeFinished.finish(result);
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
    public void init(NettyConfiguration nettyConfiguration) {
        register(nettyConfiguration.getObjectWrapList());
    }


}
