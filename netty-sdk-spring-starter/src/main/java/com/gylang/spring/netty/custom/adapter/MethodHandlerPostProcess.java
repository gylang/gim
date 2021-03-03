package com.gylang.spring.netty.custom.adapter;

import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.spring.netty.custom.method.ControllerMethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
public interface MethodHandlerPostProcess {

    /**
     * 判断是否支持处理类型
     *
     * @param controllerMethodMeta 方法
     * @return 支持true 不支持 false
     */
    boolean support(ControllerMethodMeta controllerMethodMeta);

    /**
     * 后置处理器, 当调用业务处理器之后, 调用后置处理器进行处理
     *
     * @param ctx                 netty chandler对象
     * @param me                  用户会话对象
     * @param message             统一交互消息体
     * @param methodArgumentValue 方法对用参数列表聚合对象
     * @param processResult       业务方法处理结果
     * @return 判断是否终止执行(不执行后续处理器)
     */
    boolean handler(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue, Object processResult);
}
