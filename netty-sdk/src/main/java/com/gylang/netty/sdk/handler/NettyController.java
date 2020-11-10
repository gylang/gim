package com.gylang.netty.sdk.handler;

import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.IMSession;

/**
 * @author gylang
 * data 2020/11/6
 * @version v0.0.1
 * 内部进行数据类型的转换, 规约用于实现业务功能
 * @see NettyHandler 通过注解区分业务控制器的的类型
 */
public interface NettyController<T> {


    /**
     * 处理收到客户端从长链接发送的数据
     */
    void process(IMSession me, T requestBody);
}
