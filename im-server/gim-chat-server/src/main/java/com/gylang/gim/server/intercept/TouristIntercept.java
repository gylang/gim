package com.gylang.gim.server.intercept;

import cn.hutool.core.util.StrUtil;
import com.gylang.netty.sdk.common.AfterConfigInitialize;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.intercept.NettyIntercept;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author gylang
 * data 2021/4/2
 */
@Component
@Slf4j
@Order(1)
public class TouristIntercept implements NettyIntercept, AfterConfigInitialize {

    private Set<String> nonAuthCMd = null;

    @Override
    public void init(NettyConfiguration configuration) {
        nonAuthCMd = configuration.getNettyProperties().getNonAuthCmd();
    }

    @Override
    public boolean support(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {
        return true;
    }

    @Override
    public boolean doBefore(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {

        boolean hasAuth = nonAuthCMd.contains(message.getCmd()) || StrUtil.isNotEmpty(me.getAccount());
        if (log.isDebugEnabled()) {
            log.debug("[ 非法消息拦截 ] : 是否授权接入 : {}, 发送的消息体 : {}", hasAuth ? me.getAccount() : hasAuth, message);
        }
        return !hasAuth;
    }

    @Override
    public Object doAfter(ChannelHandlerContext ctx, IMSession me, MessageWrap message, Object result) {
        return result;
    }
}
