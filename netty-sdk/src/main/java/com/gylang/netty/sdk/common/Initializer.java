package com.gylang.netty.sdk.common;

import com.gylang.netty.sdk.config.NettyConfiguration;

/**
 * @author gylang
 * data 2021/3/2
 */
public interface Initializer {
    /**
     * 初始化方法
     *
     * @param configuration 初始化配置
     */
    void init(NettyConfiguration configuration);
}
