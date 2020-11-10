package com.gylang.netty.sdk.handler;

import com.gylang.netty.sdk.call.MessagePusher;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;

import java.util.List;

/**
 * netty分发处理适配器 责任链是处理 IMRequestAdapter
 * @see com.gylang.netty.sdk.handler.IMRequestAdapter
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
public class SimpleAdapterDispatch implements IMRequestAdapter {

    @Setter
    private List<IMRequestAdapter> requestAdapterList;

    @Override
    public void process(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MessagePusher messagePusher) {

        for (IMRequestAdapter adapter : requestAdapterList) {
            adapter.process(ctx, me, message, messagePusher);
        }
    }
}
