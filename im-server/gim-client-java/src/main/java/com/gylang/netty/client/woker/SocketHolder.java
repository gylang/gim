package com.gylang.netty.client.woker;

import com.gylang.netty.client.call.ICall;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gylang
 * data 2021/3/30
 */
public class SocketHolder {


    private static ServerSocket socket = null;

    private static Map<String, List<ICall<?>>> callListener = new HashMap<>();

    private static Thread readWorker;

    public static void connect(String ip, Integer port, ICall<String> call) {

        try {
            socket.bind(new InetSocketAddress(ip, port));
            Socket accept = socket.accept();
            readWorker = new ReadWorkerThread(accept);
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
