package com.gylang.netty.client;

import com.gylang.netty.client.woker.SocketHolder;
import com.gylang.netty.client.woker.SocketManager;

/**
 * @author gylang
 * data 2021/3/31
 */
public class SocketClientApplication {


    public static void run() {

        // 连接socket
        SocketManager socketManager = SocketHolder.getInstance();
        socketManager.connect("127.0.0.1", 46001, str -> {
            if ("1".equals(str)) {
                System.out.println("连接成功！");
            } else {
                System.out.println(str);
            }
        });



    }


    public static void main(String[] args) {
        Thread thread = new Thread(() -> run());
        thread.setDaemon(false);
        thread.start();
    }
}
