package com.gylang.netty.sdk.handler;

import com.gylang.netty.sdk.annotation.AdapterType;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.repo.FillUserInfoContext;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;

import java.util.List;

/**
 * netty分发处理适配器 责任链是处理 IMRequestAdapter
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 * @see com.gylang.netty.sdk.handler.IMRequestAdapter
 */
@AdapterType(isDispatch = true)
public class DefaultAdapterDispatch implements IMRequestAdapter {

    @Setter
    private List<IMRequestAdapter> requestAdapterList;
    @Setter
    private FillUserInfoContext fillUserInfoContext;

    @Override
    public void process(ChannelHandlerContext ctx, IMSession me, MessageWrap message, NotifyProvider messagePusher) {
        if (null != fillUserInfoContext) {
            fillUserInfoContext.fill(me);
        }
        for (IMRequestAdapter adapter : requestAdapterList) {
            adapter.process(ctx, me, message, messagePusher);
        }
    }
}
