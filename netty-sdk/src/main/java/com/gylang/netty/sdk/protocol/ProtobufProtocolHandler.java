package com.gylang.netty.sdk.protocol;

import com.gylang.netty.sdk.config.NettyConfiguration;

/**
 * @author gylang
 * data 2021/4/29
 */
public class ProtobufProtocolHandler implements ProtocolHandler{
    @Override
    public byte version() {
        return 0;
    }

    @Override
    public byte protocol() {
        return 2;
    }

    @Override
    public Object handle(byte version, short server, byte[] byteBuf) {
        return null;
    }

    @Override
    public void init(NettyConfiguration configuration) {

    }
}
