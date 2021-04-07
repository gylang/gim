//package com.gylang.netty.client.rubbish;
//
//import com.gylang.netty.client.call.ICall;
//import lombok.extern.slf4j.Slf4j;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author gylang
// * data 2020/11/17
// */
//@Slf4j
//public class MyWebSocketClient extends WebSocketClient {
//
//
//    private static Map<String, List<ICall<?>>> callListener = new HashMap<>();
//
//    public MyWebSocketClient(URI serverUri) {
//        super(serverUri);
//    }
//
//    @Override
//    public boolean connectBlocking() throws InterruptedException {
//        return super.connectBlocking();
//    }
//
//    public static synchronized void bind(int type, String key, ICall<?> call) {
//
//        String lk = getListenKey(type, key);
//        List<ICall<?>> callList = callListener.computeIfAbsent(lk, k -> new ArrayList<>());
//        callList.add(call);
//
//    }
//
//    @Override
//    public void reconnect() {
//        super.reconnect();
//    }
//
//    public static synchronized void unBind(int type, String key, ICall<?> call) {
//
//        String lk = getListenKey(type, key);
//        List<ICall<?>> callList = callListener.get(lk);
//        if (null != callList) {
//            callList.remove(call);
//        }
//
//    }
//
//    private static String getListenKey(int type, String key) {
//        return type + "-" + key;
//    }
//
//    @Override
//    public void onOpen(ServerHandshake serverHandshake) {
//
//        log.info("websocket open");
//    }
//
//    @Override
//    public void onMessage(String s) {
//
//        log.info("websocket onMessage : {}", s);
//
//    }
//
//    @Override
//    public void onClose(int i, String s, boolean b) {
//
//        log.info("websocket onClose : {}", s);
//    }
//
//    @Override
//    public void onError(Exception e) {
//
//        log.info("websocket onError : {}", e.getMessage());
//
//    }
//}
