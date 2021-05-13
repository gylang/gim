package com.gylang.gim.admin.config;


/**
 * @author gylang
 * data 2021/3/2
 */
public interface AfterConfigInitialize {
    /**
     * 初始化方法
     *
     * @param configuration 初始化配置
     */
    void init(NettyConfiguration configuration);
}
