package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.MessagePusher;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
@Setter
@Getter
public class MethodMeta {

    private Method method;

    private Object object;

    public Object invoke(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MessagePusher messagePusher) {

        return null;
    }
}
