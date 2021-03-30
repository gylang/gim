package com.gylang.netty.client.woker;

import com.gylang.netty.client.call.ICall;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author gylang
 * data 2021/3/30
 */
public class SocketHolder {


    private static final int WRITE_BUFFER_SIZE = 1024;
    private static final int  READ_BUFFER_SIZE = 2048;
    private static final int CONNECT_TIME_OUT = 10 * 1000;
    private static SocketChannel socketChannel = null;

    private static Map<String, List<ICall<?>>> callListener = new HashMap<>();
    private static final ByteBuffer headerBuffer = ByteBuffer.allocate(3);

    private static Thread readWorker;
    private static final ExecutorService WORKEREXECUTOR = Executors.newFixedThreadPool(1, r -> new Thread(r, "worker-"));

    private static final ExecutorService BOSSEXECUTOR = Executors.newFixedThreadPool(1, r -> new Thread(r, "boss-"));
    public static void connect(String ip, Integer port, ICall<String> call) {

        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.socket().setTcpNoDelay(true);
            socketChannel.socket().setKeepAlive(true);
            socketChannel.socket().setReceiveBufferSize(READ_BUFFER_SIZE);
            socketChannel.socket().setSendBufferSize(WRITE_BUFFER_SIZE);

            socketChannel.socket().connect(new InetSocketAddress(ip, port), CONNECT_TIME_OUT);

            /*
             *开始读取来自服务端的消息，先读取3个字节的消息头
             */
            while (socketChannel.read(headerBuffer) > 0) {
//                handleSocketReadEvent();
            }
            call.call("1");
        } catch (IOException e) {
            e.printStackTrace();
            call.call(e.getMessage());
        }
    }

    public static synchronized void bind(int type, String key, ICall<?> call) {

        String lk = getListenKey(type, key);
        List<ICall<?>> callList = callListener.computeIfAbsent(lk, k -> new ArrayList<>());
        callList.add(call);

    }

    public static synchronized void unBind(int type, String key, ICall<?> call) {

        String lk = getListenKey(type, key);
        List<ICall<?>> callList = callListener.get(lk);
        if (null != callList) {
            callList.remove(call);
        }

    }

    private static String getListenKey(int type, String key) {
        return type + "-" + key;
    }
}
