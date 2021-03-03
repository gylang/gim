/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.gylang.netty.sdk.handler;

import com.gylang.netty.sdk.common.Initializer;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import io.netty.channel.ChannelHandlerContext;

import java.util.Comparator;
import java.util.List;

/**
 * netty请求适配器, 规约用于系统内部适配实现, 非业务功能
 */


public interface IRequestAdapter<T> extends Initializer , Comparator<IRequestAdapter<?>> {

    int DEFAULT_ORDER = Integer.MAX_VALUE >> 1;

    /**
     * 处理收到客户端从长链接发送的数据
     *
     * @param ctx 上下文
     * @param me 当前会话
     * @param message 消息体
     */
    Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message);

    /**
     * 注册
     *
     * @param processList 注册参数列表
     */
    void register(List<ObjectWrap> processList);





    /**
     * 排序权重 小在前 大在后
     * @return 排序权重
     */
    Integer order();

    @Override
    default int compare(IRequestAdapter<?> o1, IRequestAdapter<?> o2) {
        int order1 = null == o1.order() ? DEFAULT_ORDER : o1.order();
        int order2 = null == o2.order() ? DEFAULT_ORDER : o2.order();
        return Integer.compare(order1, order2);

    }

}
