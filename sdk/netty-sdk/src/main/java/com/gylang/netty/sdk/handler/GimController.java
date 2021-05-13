package com.gylang.netty.sdk.handler;

import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.GIMSession;

/**
 * @author gylang
 * data 2020/11/6
 * @version v0.0.1
 * 内部进行数据类型的转换, 规约用于实现业务功能
 * @see NettyHandler 通过注解区分业务控制器的的类型
 */
public interface GimController<T> {


    /**
     * 处理收到客户端从长链接发送的数据
     *
     * @param me          当前会话用户
     * @param requestBody MessageWrap -> content -> 类型转换
     * @return 命令处理结果
     */
    Object process(GIMSession me, T requestBody);
}
