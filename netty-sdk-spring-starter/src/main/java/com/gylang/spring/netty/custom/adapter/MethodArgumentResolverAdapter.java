package com.gylang.spring.netty.custom.adapter;

import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
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
public class MethodArgumentResolverAdapter{
    @Autowired(required = false)
    private List<MethodArgumentResolverHandler> methodArgumentResolverHandlerList;
    @Autowired(required = false)
    private List<MethodHandlerPostProcess> methodHandlerPostProcessList;

    public void handler(ChannelHandlerContext ctx, IMSession me, MessageWrap message, MethodArgumentValue methodArgumentValue) {

        // 解析通信信息
        for (MethodArgumentResolverHandler methodArgumentResolverHandler : methodArgumentResolverHandlerList) {
            boolean support = methodArgumentResolverHandler.support(methodArgumentValue.getMethodMeta());
            if (support) {
                boolean continueResolve = methodArgumentResolverHandler.handler(ctx, me, message, methodArgumentValue);
                if (!continueResolve) {
                    break;
                }
            }
        }
        //调用业务方法
        Object processResult = methodArgumentValue.invoke();

        // 执行后置处理器
        if (null == methodHandlerPostProcessList) {
            return;
        }
        for (MethodHandlerPostProcess methodHandlerPostProcess : methodHandlerPostProcessList) {
            boolean support = methodHandlerPostProcess.support(methodArgumentValue.getMethodMeta());
            if (support) {
                boolean continueResolve = methodHandlerPostProcess.handler(ctx, me, message, methodArgumentValue, processResult);
                if (!continueResolve) {
                    break;
                }
            }
        }
    }

}
