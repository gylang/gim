package com.gylang.spring.netty.custom.handler;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.spring.netty.custom.method.ControllerMethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import io.netty.channel.ChannelHandlerContext;

/**
 * todo 统一调用注册方法, 初始化support 实现反射方法cache 快速方法
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */

public interface MethodArgumentResolverHandler {

    /**
     * 判断是否支持处理类型
     *
     * @param controllerMethodMeta 方法
     * @return 支持true 不支持 false
     */
    boolean support(ControllerMethodMeta controllerMethodMeta);

    /**
     * 解析请求参数, 赋值到 methodArgumentValue 设置参数列表
     *
     * @param ctx                 netty chandler对象
     * @param me                  用户会话对象
     * @param message             统一交互消息体
     * @param methodArgumentValue 方法对用参数列表聚合对象
     * @return 判断是否终止执行(不执行后续处理器)
     */
    boolean handler(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue);
}
