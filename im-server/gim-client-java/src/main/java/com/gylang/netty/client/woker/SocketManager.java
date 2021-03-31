package com.gylang.netty.client.woker;

import com.gylang.netty.client.call.ICall;
import com.gylang.netty.client.coder.ClientMessageDecoder;
import com.gylang.netty.client.coder.ClientMessageEncoder;
import com.gylang.netty.client.constant.CommonConstant;
import com.gylang.netty.client.domain.MessageWrap;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author gylang
 * data 2021/3/31
 */
public class SocketManager {

    private int WRITE_BUFFER_SIZE = 1024;
    private int READ_BUFFER_SIZE = 2048;
    private int CONNECT_TIME_OUT = 10 * 1000;
    private SocketChannel socketChannel = null;

    /**
     * 定时任务扫码间隔
     */
    private int checkInterval = 5 * 1000;
    /**
     * 扫码最低时间间隔
     */
    private int messagesValidTime = 2 * 1000;

    private Map<String, List<ICall<?>>> callListener = new HashMap<>();


    private final ByteBuffer headerBuffer = ByteBuffer.allocate(3);
    private ByteBuffer readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);

    private Thread readWorker;
    private final ExecutorService WORKER_EXECUTOR =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>(), new DefaultThreadFactory("worker"));

    private final ExecutorService BOSS_EXECUTOR =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>(), new DefaultThreadFactory("boss"));

    private final ExecutorService LISTENER_EXECUTOR =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>(), new DefaultThreadFactory("listener"));


    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.AbortPolicy());


    private final ClientMessageDecoder messageDecoder = new ClientMessageDecoder();
    private final ClientMessageEncoder messageEncoder = new ClientMessageEncoder();

    public void connect(String ip, Integer port, ICall<String> call) {

        BOSS_EXECUTOR.execute(() -> {
            try {
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(true);
                socketChannel.socket().setTcpNoDelay(true);
                socketChannel.socket().setKeepAlive(true);
                socketChannel.socket().setReceiveBufferSize(READ_BUFFER_SIZE);
                socketChannel.socket().setSendBufferSize(WRITE_BUFFER_SIZE);

                socketChannel.socket().connect(new InetSocketAddress(ip, port), CONNECT_TIME_OUT);

                scheduledExecutorService.scheduleAtFixedRate(this::sendHeart,
                        checkInterval,
                        checkInterval,
                        TimeUnit.MILLISECONDS);
                /*
                 *开始读取来自服务端的消息，先读取3个字节的消息头
                 */
                call.call("1");
                while (socketChannel.read(headerBuffer) > 0) {
                    handleSocketReadEvent();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("尝试重连");
                connect(ip, port, call);
                call.call(e.getMessage());
            }
        });

    }

    private void sendHeart() {
        if (socketChannel.isOpen()) {
            System.out.println("[发送心跳] : 连接正常");
            send(CommonConstant.HEART);
        } else {

            System.out.println("[发送心跳] : 连接异常");
        }
    }

    private void handleSocketReadEvent() throws IOException {

        Object message = messageDecoder.doDecode(headerBuffer, socketChannel);

//        LOGGER.messageReceived(socketChannel, message);

        if (CommonConstant.HEART == message) {
            send(CommonConstant.HEART);
            return;
        }

        this.messageReceived(message);


    }

    public void send(final MessageWrap body) {

        if (!isConnected()) {
            return;
        }

        WORKER_EXECUTOR.execute(() -> {
            int result = 0;
            try {

                ByteBuffer buffer = messageEncoder.encode(body);
                while (buffer.hasRemaining()) {
                    result += socketChannel.write(buffer);
                }

            } catch (Exception e) {
                result = -1;
            } finally {

                if (result <= 0) {
                    closeSession();
                } else {
                    messageSent(body);
                }
            }
        });
    }


    public void messageReceived(Object data) {

        if (data instanceof MessageWrap) {

            sendBroadcast((MessageWrap) data);

        }
    }

    public void messageSent(MessageWrap data) {


        if (data != null) {
            sendBroadcast(data);
        }
    }

    public void destroy() {
        closeSession();
    }

    public boolean isConnected() {
        return socketChannel != null && socketChannel.isConnected();
    }

    public void closeSession() {

        if (!isConnected()) {
            return;
        }

        try {
            socketChannel.close();
        } catch (IOException ignore) {
        } finally {
            this.sessionClosed();
        }
    }

    public void sessionClosed() {


        readBuffer.clear();

        if (readBuffer.capacity() > READ_BUFFER_SIZE) {
            readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
        }

        sendBroadcast(CommonConstant.HEART);

    }

    private void sendBroadcast(final Object intent) {

        if (null == intent) {
            return;
        }

        if (intent instanceof MessageWrap) {
            MessageWrap message = (MessageWrap) intent;
            List<ICall<?>> callList = callListener.get(getListenKey(message.getType(), message.getCmd()));
            if (null != callList) {
                for (ICall<?> iCall : callList) {
                    LISTENER_EXECUTOR.execute(() -> ((ICall<MessageWrap>) iCall).call(message));

                }
            }
        }
    }

    public synchronized void bind(int type, String key, ICall<?> call) {

        String lk = getListenKey(type, key);
        List<ICall<?>> callList = callListener.computeIfAbsent(lk, k -> new ArrayList<>());
        callList.add(call);

    }

    public synchronized void unBind(int type, String key, ICall<?> call) {

        String lk = getListenKey(type, key);
        List<ICall<?>> callList = callListener.get(lk);
        if (null != callList) {
            callList.remove(call);
        }

    }

    private String getListenKey(int type, String key) {
        return type + "-" + key;
    }
}
