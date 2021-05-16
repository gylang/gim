package com.gylang.netty.sdk.protocol;

import com.gylang.netty.sdk.api.common.AfterConfigInitialize;

/**
 * @author gylang
 * data 2021/4/29
 */
public interface ProtocolHandler extends AfterConfigInitialize {

    /**
     * 版本号
     *
     * @return 版本号
     */
    byte version();

    /**
     * 协议类型
     *
     * @return 协议类型
     */
    byte protocol();

    /**
     * 解析对象
     *
     * @param version 版本号
     * @param server  cmd/服务类型
     * @param byteBuf 数据
     * @return 协议解析结果
     */
    Object handle(byte version, short server, byte[] byteBuf);
}
