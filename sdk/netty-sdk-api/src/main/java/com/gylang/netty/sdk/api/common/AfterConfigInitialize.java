package com.gylang.netty.sdk.api.common;

import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;

/**
 * @author gylang
 * data 2021/3/2
 */
public interface AfterConfigInitialize {
    /**
     * 初始化方法 加载配置类后调用
     *
     * @param configuration 初始化配置
     */
    void init(GimGlobalConfiguration configuration);
}
