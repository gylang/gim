package com.gylang.netty.client;

import com.google.protobuf.ByteString;
import com.gylang.netty.client.domain.MessageWrapProto;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
public class MessageConsoleResolver {


    public static MessageWrapProto.Model resolve(String nextLine) {

        return MessageWrapProto.Model.newBuilder()
                .setContent(nextLine)
                .setContentBytes(ByteString.copyFromUtf8(nextLine))
                .setKey("chat")
                .build();
    }
}
