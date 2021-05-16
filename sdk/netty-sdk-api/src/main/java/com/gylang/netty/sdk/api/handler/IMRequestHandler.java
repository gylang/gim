package com.gylang.netty.sdk.api.handler;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;

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
     *
     * @param me      当前会话用户
     * @param message 消息
     * @return 命令处理结果
     */
    Object process(GIMSession me, MessageWrap message);
}
