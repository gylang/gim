package com.gylang.netty.client;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author gylang
 * data 2020/11/17
 */
@Slf4j
public class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

        log.info("websocket open");
    }

    @Override
    public void onMessage(String s) {

        log.info("websocket onMessage : {}", s);

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        this.reconnect();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        log.info("websocket onClose : {}", s);
    }

    @Override
    public void onError(Exception e) {

        this.reconnect();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        log.info("websocket onError : {}", e.getMessage());

    }
}
