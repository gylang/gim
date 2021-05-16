package com.gylang.gim.server.intercept;

import cn.hutool.core.util.StrUtil;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.common.AfterConfigInitialize;
import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.intercept.NettyIntercept;
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

    private Set<Integer> nonAuthCMd = null;

    @Override
    public void init(GimGlobalConfiguration configuration) {
        nonAuthCMd = configuration.getGimProperties().getNonAuthType();
    }

    @Override
    public boolean support(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {
        return true;
    }

    @Override
    public boolean doBefore(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {

        boolean canAccess = nonAuthCMd.contains(message.getType()) || StrUtil.isNotEmpty(me.getAccount());
        if (log.isDebugEnabled()) {
            if (!canAccess) {
                log.debug("[非法消息拦截] :NID : {}, 发送的消息体 : {}", me.getNid(), message);
            }
        }
        return !canAccess;
    }

    @Override
    public Object doAfter(ChannelHandlerContext ctx, GIMSession me, MessageWrap message, Object result) {
        return result;
    }
}
