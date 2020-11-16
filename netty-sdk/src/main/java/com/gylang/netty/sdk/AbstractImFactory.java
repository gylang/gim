package com.gylang.netty.sdk;

import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;

/**
 * 抽象im工厂
 *
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public interface AbstractImFactory {

    /**
     * 初始化工厂
     */
    void init();

    /**
     * 启动netty服务
     */
    void start();
}
