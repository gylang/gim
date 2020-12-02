package com.gylang.netty.sdk.handler;

import com.gylang.netty.sdk.call.NotifyProvider;
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
 * @see com.gylang.netty.sdk.handler.IMRequestAdapter
 */
public class DefaultAdapterDispatch implements DispatchAdapterHandler {

    @Setter
    private List<BizRequestAdapter> requestAdapterList = new ArrayList<>();
    @Setter
    private NettyUserInfoFillHandler nettyUserInfoFillHandler;

    @Override
    public void process(ChannelHandlerContext ctx, IMSession me, MessageWrap message, NotifyProvider messagePusher) {
        if (null != nettyUserInfoFillHandler) {
            nettyUserInfoFillHandler.fill(me);
        }
        for (IMRequestAdapter adapter : requestAdapterList) {
            adapter.process(ctx, me, message, messagePusher);
        }
    }

    @Override
    public void register(List<?> processList) {

        List<BizRequestAdapter> targetList = getTargetList(processList);
        for (BizRequestAdapter o : targetList) {
            if (!requestAdapterList.contains(o)) {
                requestAdapterList.add(o);
            }
        }
    }

    @Override
    public List<?> mappingList() {
        return requestAdapterList;
    }


}
