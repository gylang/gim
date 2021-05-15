package com.gylang.netty.sdk.domain;

import java.util.List;

/**
 * @author gylang
 * data 2021/5/15
 */

public interface CrossServerObserver {

    /**
     * 获取服务
     * @param ip
     * @return
     */
     GimServerInfo getByServerIp(String ip);

    /**
     * 注册服务
     * @param gimServerInfo
     */
    void registry(GimServerInfo gimServerInfo);

    /**
     * 解绑服务
     * @param gimServerInfo
     */
    void unbind(GimServerInfo gimServerInfo);

    /**
     * 服务更新
     * @param gimServerInfo
     */
    void update(GimServerInfo gimServerInfo);

    /**
     * 获取所有的
     * @return
     */
    List<GimServerInfo> getAll();
}
