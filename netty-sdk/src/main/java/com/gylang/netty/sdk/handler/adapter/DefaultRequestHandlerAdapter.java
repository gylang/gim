package com.gylang.netty.sdk.handler.adapter;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.annotation.AdapterType;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.handler.IMRequestHandler;
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
@AdapterType(order = 100)
public class DefaultRequestHandlerAdapter implements IMRequestAdapter {


    private Map<String, IMRequestHandler> handlerMap;

    @Override
    public void process(ChannelHandlerContext ctx, IMSession me, MessageWrap message, NotifyProvider messagePusher) {

        IMRequestHandler requestHandler = handlerMap.get(message.getKey());
        if (null == requestHandler) {
            return;
        }
        requestHandler.process(me, message);
    }

    public void register(List<IMRequestHandler> requestHandlerList) {
        if (null == requestHandlerList) {
        handlerMap = CollUtil.newHashMap();
            return;
        }
        handlerMap = CollUtil.newHashMap(requestHandlerList.size());
        for (IMRequestHandler imRequestHandler : requestHandlerList) {
            NettyHandler annotation = AnnotationUtil.getAnnotation(imRequestHandler.getClass(), NettyHandler.class);
            if (null != annotation) {
                handlerMap.put(annotation.value(), imRequestHandler);
            }
        }
    }
}
