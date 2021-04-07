//package com.gylang.netty.client.rubbish;
//
//import com.gylang.netty.client.domain.MessageWrap;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Scanner;
//
///**
// * @author gylang
// * data 2020/11/17
// */
//public class JsonClient {
//
//    public static void main(String[] args) throws URISyntaxException, InterruptedException {
//
//        System.out.println("输入连接用户名");
//        String name = new Scanner(System.in).next();
//
//        MyWebSocketClient webSocketClient = new MyWebSocketClient(new URI("ws://localhost:46000/ws"));
//        webSocketClient.connectBlocking();
//        MessageWrap messageWrap = new MessageWrap();
//        messageWrap.setCmd("login");
//        messageWrap.setContent(name);
////        webSocketClient.send(JSON.toJSONString(messageWrap));
////        new Thread(() -> {
////            while (true) {
////                if (webSocketClient.isOpen()) {
////                    try {
////                        TimeUnit.SECONDS.sleep(3);
////                        try {
////                            webSocketClient.sendPing();
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                        System.out.println("发送heart");
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                } else {
////                    try {
////                        System.out.println("尝试重连");
////                        webSocketClient.reconnectBlocking();
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        }, "心跳").start();
////
////        while (true) {
////            if (webSocketClient.isOpen()) {
////
////                messageWrap.setContent(UUID.fastUUID().toString());
////                messageWrap.setCmd("chat");
////                try {
////                    webSocketClient.send(JSON.toJSONString(messageWrap));
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                System.out.println("模拟消息");
////                TimeUnit.SECONDS.sleep(5L);
////            }
////        }
//
//    }
//}
