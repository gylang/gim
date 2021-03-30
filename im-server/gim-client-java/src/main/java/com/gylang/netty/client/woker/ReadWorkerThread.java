package com.gylang.netty.client.woker;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
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

        if (null != socket) {
            InputStream inputStream = null;
            SocketChannel channel = socket.getChannel();

        }
    }
}
