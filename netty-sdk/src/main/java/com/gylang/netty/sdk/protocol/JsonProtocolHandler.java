package com.gylang.netty.sdk.protocol;

import com.gylang.netty.sdk.config.GimGlobalConfiguration;

/**
 * @author gylang
 * data 2021/4/29
 */
public class JsonProtocolHandler implements ProtocolHandler {

    @Override
    public byte version() {
        return 0;
    }

    @Override
    public byte protocol() {
        return 1;
    }

    @Override
    public Object handle(byte version, short server, byte[] byteBuf) {

        // 不支持泛型
        return null;
    }

    @Override
    public void init(GimGlobalConfiguration configuration) {
        // todo
    }
}
