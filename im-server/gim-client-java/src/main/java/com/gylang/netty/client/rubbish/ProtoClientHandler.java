package com.gylang.netty.client.rubbish;

import com.gylang.netty.client.domain.MessageWrapProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoClientHandler extends SimpleChannelInboundHandler<MessageWrapProto.Model> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageWrapProto.Model msg) throws Exception {

        System.out.println(msg.getContent());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(ctx);



    }
}
