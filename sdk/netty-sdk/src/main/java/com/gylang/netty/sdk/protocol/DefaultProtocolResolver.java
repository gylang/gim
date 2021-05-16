package com.gylang.netty.sdk.protocol;

import com.gylang.netty.sdk.api.common.AfterConfigInitialize;
import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gylang
 * data 2021/4/29
 */
public class DefaultProtocolResolver implements ProtocolResolver, AfterConfigInitialize {

    private Map<String, ProtocolHandler> protocolHandlerMap = new HashMap<>();

    @Override
    public Object resolve(byte version, byte magic, short server, byte[] byteBuf) {
        return null;
    }

    @Override
    public void init(GimGlobalConfiguration configuration) {

        // 注册内置两个协议处理器
        JsonProtocolHandler jsonProtocolHandler = new JsonProtocolHandler();
        ProtobufProtocolHandler protobufProtocolHandler = new ProtobufProtocolHandler();

        // 调用初始化
        jsonProtocolHandler.init(configuration);
        protobufProtocolHandler.init(configuration);

    }
}
