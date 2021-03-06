package com.gylang.netty.sdk.api.intercept;

import cn.hutool.core.collection.CollUtil;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author gylang
 * data 2021/2/21
 */
public interface NettyIntercept {
    /**
     * 支持判断
     *
     * @param ctx     上下文
     * @param me      当前会话
     * @param message 消息体
     * @return 支持 true
     */
    boolean support(ChannelHandlerContext ctx, GIMSession me, MessageWrap message);

    /**
     * 请求前置拦截器
     *
     * @param ctx     上下文
     * @param me      当前会话
     * @param message 消息体
     * @return true 拦截消息
     */
    boolean doBefore(ChannelHandlerContext ctx, GIMSession me, MessageWrap message);

    /**
     * 请求后置拦截器
     *
     * @param ctx     上下文
     * @param me      当前会话
     * @param message 消息体
     * @param result  处理结果
     * @return 拦截后结果
     */
    Object doAfter(ChannelHandlerContext ctx, GIMSession me, MessageWrap message, Object result);

    /**
     * 处理收到客户端从长链接发送的数据
     *
     * @param interceptList 拦截器
     * @param ctx           上下文
     * @param me            当前会话
     * @param message       消息体
     */
    static boolean before(List<NettyIntercept> interceptList, ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {
        boolean intercept = false;
        if (CollUtil.isEmpty(interceptList)) {
            return intercept;
        }
        for (NettyIntercept nettyIntercept : interceptList) {
            if (nettyIntercept.support(ctx, me, message)) {
                intercept = intercept || nettyIntercept.doBefore(ctx, me, message);
            }
        }
        return intercept;
    }

    /**
     * 处理收到客户端从长链接发送的数据
     *
     * @param interceptList 拦截器
     * @param ctx           上下文
     * @param me            当前会话
     * @param message       消息体
     * @param result        处理结果
     * @return 拦截后结果
     */
    static Object after(List<NettyIntercept> interceptList, ChannelHandlerContext ctx, GIMSession me, MessageWrap message, Object result) {
        if (CollUtil.isEmpty(interceptList)) {
            return result;
        }
        for (NettyIntercept nettyIntercept : interceptList) {
            if (nettyIntercept.support(ctx, me, message)) {
                result = nettyIntercept.doAfter(ctx, me, message, result);
            }
        }
        return result;
    }
}
