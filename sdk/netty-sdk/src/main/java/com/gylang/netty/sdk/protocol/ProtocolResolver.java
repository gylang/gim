package com.gylang.netty.sdk.protocol;

import com.gylang.netty.sdk.api.common.AfterConfigInitialize;

/**
 * 私有协议解析器
 *
 * @author gylang
 * data 2021/4/29
 */
public interface ProtocolResolver extends AfterConfigInitialize {


    /**
     * 协议解析器
     *
     * @param version 版本号
     * @param magic   魔数
     * @param server  服务类型
     * @param byteBuf 消息体
     * @return 协议解析结果
     */
    Object resolve(byte version, byte magic, short server, byte[] byteBuf);
}
