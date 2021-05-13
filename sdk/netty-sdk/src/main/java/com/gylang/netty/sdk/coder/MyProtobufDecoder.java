package com.gylang.netty.sdk.coder;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author gylang
 * data 2021/2/13
 */
public class MyProtobufDecoder extends MessageToMessageDecoder<ByteBuf> {

    private  MessageLite prototype;
    private  ExtensionRegistryLite extensionRegistry;



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {


    }


}
