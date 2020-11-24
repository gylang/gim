package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.factory.AbstractImFactory;

/**
 * im服务应用上下文 带有消息推送
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public interface ImApplicationContext extends AbstractImFactory, NotifyProvider {


}
