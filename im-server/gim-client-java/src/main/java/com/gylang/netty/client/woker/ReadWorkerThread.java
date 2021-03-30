package com.gylang.netty.client.woker;

import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * @author gylang
 * data 2021/3/30
 */
public class ReadWorkerThread extends Thread {

    private Socket socket;

    public ReadWorkerThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {

        socket.
    }
}
