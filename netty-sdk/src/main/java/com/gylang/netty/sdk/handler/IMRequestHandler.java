package com.gylang.netty.sdk.handler;

import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;

/**
 * 业务服务处理类, message.key 来确定调用的服务类型, 规约用于实现业务功能
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 * @see NettyHandler 标识handler处理的服务类型
 */
public interface IMRequestHandler {

    /**
     * 处理收到客户端从长链接发送的数据
     */
    Object process(IMSession me, MessageWrap message);
}
