package com.gylang.gim.client;

import com.gylang.gim.client.util.HttpUtil;
import com.gylang.gim.remote.SocketHolder;
import com.gylang.gim.remote.SocketManager;

/**
 * @author gylang
 * data 2021/3/31
 */
public class SocketClientApplication {


    public static void run() {



        // 初始化 retrofit
        HttpUtil.init();


    }


    public static void main(String[] args) {
        Thread thread = new Thread(() -> run());
        thread.setDaemon(false);
        thread.start();
    }
}
