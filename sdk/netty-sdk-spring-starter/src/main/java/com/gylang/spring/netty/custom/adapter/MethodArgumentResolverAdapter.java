package com.gylang.spring.netty.custom.adapter;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.common.InvokeFinished;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.spring.netty.custom.handler.MethodArgumentResolverHandler;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Component
public class MethodArgumentResolverAdapter {
    /** 方法参数解析器 */
    @Autowired(required = false)
    private List<MethodArgumentResolverHandler> methodArgumentResolverHandlerList;
    /** 方法执行通知 */
    @Autowired(required = false)
    private List<MethodHandlerPostProcess> methodHandlerPostProcessList;

    public Object handler(ChannelHandlerContext ctx, GIMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue) {

        // 解析通信信息
        for (MethodArgumentResolverHandler methodArgumentResolverHandler : methodArgumentResolverHandlerList) {
            boolean support = methodArgumentResolverHandler.support(methodArgumentValue.getControllerMethodMeta());
            if (support) {
                boolean endResolve = methodArgumentResolverHandler.handler(ctx, me, message, methodArgumentValue);
                if (endResolve) {
                    break;
                }
            }
        }
        //调用业务方法
        Object processResult = methodArgumentValue.invoke();
        processResult = null != processResult ? processResult : InvokeFinished.getInstance();
        // 执行后置处理器
        if (null == methodHandlerPostProcessList) {
            return processResult;
        }
        for (MethodHandlerPostProcess methodHandlerPostProcess : methodHandlerPostProcessList) {
            boolean support = methodHandlerPostProcess.support(methodArgumentValue.getControllerMethodMeta());
            if (support) {
                boolean continueResolve = methodHandlerPostProcess.handler(ctx, me, message, methodArgumentValue, processResult);
                if (!continueResolve) {
                    break;
                }
            }
        }
        return processResult;
    }

}
