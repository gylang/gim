package com.gylang.chat;

import com.gylang.netty.sdk.ImApplicationContext;

/**
 * @author gylang
 * data 2020/11/10
 * @version v0.0.1
 */
public class ChatApplication {

    public static void main(String[] args) {

        // 启动服务
        TestStartConfig testStartConfig = new TestStartConfig();
        ImApplicationContext imApplicationContext = testStartConfig.imApplicationContext();
    }
}
