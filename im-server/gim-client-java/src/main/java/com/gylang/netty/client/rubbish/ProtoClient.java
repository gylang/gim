package com.gylang.netty.client.rubbish;

import com.gylang.netty.client.domain.MessageWrapProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.Scanner;

/**
 * protobuf 客户端
 */
public class ProtoClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            //handler
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new MyProtoClientInitializer());
            final Channel channel = bootstrap.connect("127.0.0.1", 46000).sync().channel();
            System.out.println("输出登录人");

            String name = new Scanner(System.in).next();
            MessageWrapProto.Model register = MessageWrapProto.Model.newBuilder()
                    .setKey("register")
                    .setContent(name)
                    .build();
            channel.writeAndFlush(register);

            for (; ; ) {

                Scanner scanner = new Scanner(System.in);
                MessageWrapProto.Model message = MessageConsoleResolver.resolve(scanner.next());
                channel.writeAndFlush(message);
            }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
