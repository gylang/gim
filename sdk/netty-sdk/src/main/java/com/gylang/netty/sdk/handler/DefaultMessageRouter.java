package com.gylang.netty.sdk.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.api.constant.ContentType;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.annotation.AdapterType;
import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.BizRequestAdapter;
import com.gylang.netty.sdk.api.handler.IMessageRouter;
import com.gylang.netty.sdk.api.handler.IRequestAdapter;
import com.gylang.netty.sdk.api.intercept.NettyIntercept;
import com.gylang.netty.sdk.api.repo.NettyUserInfoFillHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * netty分发处理适配器 责任链是处理 IMRequestAdapter
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 * @see IRequestAdapter
 */
@AdapterType(order = 1)
@Slf4j
public class DefaultMessageRouter implements IMessageRouter {

    private List<BizRequestAdapter> requestAdapterList = new ArrayList<>();
    private NettyUserInfoFillHandler nettyUserInfoFillHandler;
    private List<NettyIntercept> nettyInterceptList;

    @Override
    public Object process(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {


        if (ContentType.BATCH.equals(message.getContentType())) {
            // 批量消息
            String batchMessageStr = message.getContent();
            List<String> batchMessage = JSON.parseObject(batchMessageStr, new TypeReference<List<String>>() {
            });
            for (String messageWrapStr : batchMessage) {
                try {
                    process(ctx, me, JSON.parseObject(messageWrapStr, MessageWrap.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // 填充用户信息
        if (null != nettyUserInfoFillHandler) {
            nettyUserInfoFillHandler.fill(me);
        }
        Object object = null;
        boolean intercept = NettyIntercept.before(nettyInterceptList, ctx, me, message);
        if (intercept) {
            // 消息被拦截
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("[接收到消息] : {}", message);
        }

        // 消息适配器
        for (IRequestAdapter adapter : requestAdapterList) {
            object = adapter.process(ctx, me, message);
            if (null != object) {
                break;
            }
        }
        return NettyIntercept.after(nettyInterceptList, ctx, me, message, object);
    }




    @Override
    public Integer order() {
        return null;
    }

    @Override
    public void init(GimGlobalConfiguration gimGlobalConfiguration) {
        this.nettyUserInfoFillHandler = gimGlobalConfiguration.getNettyUserInfoFillHandler();
        this.nettyInterceptList = gimGlobalConfiguration.getNettyInterceptList();
        this.requestAdapterList = gimGlobalConfiguration.getBizRequestAdapterList();
    }
}
