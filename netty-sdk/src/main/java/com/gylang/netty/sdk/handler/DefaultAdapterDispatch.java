package com.gylang.netty.sdk.handler;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.annotation.AdapterType;
import com.gylang.netty.sdk.intercept.NettyIntercept;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * netty分发处理适配器 责任链是处理 IMRequestAdapter
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 * @see IRequestAdapter
 */
@AdapterType(order = 1)
public class DefaultAdapterDispatch implements DispatchAdapterHandler {

    private List<BizRequestAdapter<?>> requestAdapterList = new ArrayList<>();
    private NettyUserInfoFillHandler nettyUserInfoFillHandler;
    private List<NettyIntercept> nettyInterceptList;

    @Override
    public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {

        if (null != nettyUserInfoFillHandler) {
            nettyUserInfoFillHandler.fill(me);
        }
        Object object = null;
        NettyIntercept.before(nettyInterceptList, ctx, me, message);
        for (IRequestAdapter<?> adapter : requestAdapterList) {
            object = adapter.process(ctx, me, message);
            if (null != object) {
                break;
            }
        }
        return NettyIntercept.after(nettyInterceptList, ctx, me, message, object);
    }


    @Override
    public void register(List<ObjectWrap> processList) {

        throw new RuntimeException("不支持当前方式注册");
    }

    @Override
    public Integer order() {
        return null;
    }

    @Override
    public void init(NettyConfiguration nettyConfiguration) {
        this.nettyUserInfoFillHandler = nettyConfiguration.getNettyUserInfoFillHandler();
        this.nettyInterceptList = nettyConfiguration.getNettyInterceptList();
        this.requestAdapterList = nettyConfiguration.getBizRequestAdapterList();
    }
}
