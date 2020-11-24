package com.gylang.netty.client;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.gylang.netty.client.domain.MessageWrap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author gylang
 * data 2020/11/17
 */
public class JsonClient {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {

        System.out.println("输入连接用户名");
        String name = new Scanner(System.in).next();

        MyWebSocketClient webSocketClient = new MyWebSocketClient(new URI("ws://localhost:46000/ws"));
        webSocketClient.connectBlocking();
        MessageWrap messageWrap = new MessageWrap();
        messageWrap.setKey("login");
        messageWrap.setContent(name);
        webSocketClient.send(JSON.toJSONString(messageWrap));
        new Thread(() -> {
            while (true) {
                if (true) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        webSocketClient.sendPing();
                        System.out.println("发送heart");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "心跳").start();

        while (true) {
            if (true) {

                messageWrap.setContent(UUID.fastUUID().toString());
                messageWrap.setKey("chat");
                webSocketClient.send(JSON.toJSONString(messageWrap));
                TimeUnit.SECONDS.sleep(5L);
            }
        }

    }
}
