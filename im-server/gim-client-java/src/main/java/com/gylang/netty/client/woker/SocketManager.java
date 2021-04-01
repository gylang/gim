package com.gylang.netty.client.woker;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.gylang.netty.client.call.ICall;
import com.gylang.netty.client.coder.ChatTypeEnum;
import com.gylang.netty.client.coder.ClientMessageDecoder;
import com.gylang.netty.client.coder.ClientMessageEncoder;
import com.gylang.netty.client.constant.CommonConstant;
import com.gylang.netty.client.constant.cmd.PrivateChatCmd;
import com.gylang.netty.client.domain.MessageWrap;
import com.gylang.netty.client.enums.BaseResultCode;
import com.gylang.netty.client.util.Store.UserStore;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * socket管理
 *
 * @author gylang
 * data 2021/3/31
 */
@Data
@Slf4j
public class SocketManager {

    private int writeBufferSize = 1024;
    private int readBufferSize = 2048;
    private int connectTimeOut = 10 * 1000;
    private SocketChannel socketChannel = null;
    private volatile boolean open = true;
    private AtomicInteger login = new AtomicInteger(0);
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
    private ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);

    /** 工作线程 处理消息的发送 */
    private final ExecutorService workerExecutor =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNamePrefix("worker").build());
    /** 处理消息的连接 消息的接收 */
    private final ExecutorService bossExecutor =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNamePrefix("boss").build());
    /** 消息的分发 */
    private final ExecutorService listenerExecutor =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNamePrefix("listener").build());

    /** 定时任务心跳 */
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.AbortPolicy());

    /** 消息解码 */
    private final ClientMessageDecoder messageDecoder = new ClientMessageDecoder();
    /** 消息编码 */
    private final ClientMessageEncoder messageEncoder = new ClientMessageEncoder();

    public void connect(String ip, Integer port, ICall<String> call) {

        bossExecutor.execute(() -> {
            while (open) {
                try {
                    doConnect(ip, port, call);
                    System.out.println("尝试重连");
                } catch (IOException e) {
                    e.printStackTrace();
                    login.set(0);
                }
            }
        });

    }

    private void doConnect(String ip, Integer port, ICall<String> call) throws IOException {
        // 开启连接
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        socketChannel.socket().setTcpNoDelay(true);
        socketChannel.socket().setKeepAlive(true);
        socketChannel.socket().setReceiveBufferSize(readBufferSize);
        socketChannel.socket().setSendBufferSize(writeBufferSize);
        socketChannel.socket().connect(new InetSocketAddress(ip, port), connectTimeOut);
        // 登录系统
        login();
        bind(ChatTypeEnum.NOTIFY.getType(), PrivateChatCmd.SOCKET_CONNECTED, onLoginSuccess);
        // 心跳监测
        scheduledExecutorService.scheduleAtFixedRate(this::sendHeart,
                checkInterval,
                checkInterval,
                TimeUnit.MILLISECONDS);

        // 开始读取来自服务端的消息，先读取3个字节的消息头
        call.call("1");
        while (socketChannel.read(headerBuffer) > 0) {
            handleSocketReadEvent();
        }
    }

    private void sendHeart() {
        if (socketChannel.isOpen()) {
            System.out.println("[发送心跳] : 连接正常");
            if (0 == login.get()) {
                login();
            }
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

        workerExecutor.execute(() -> {
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


    public void login() {

        String token = UserStore.getInstance().getToken();
        if (StrUtil.isNotEmpty(token)) {

            send(MessageWrap.builder()
                    .type(ChatTypeEnum.PRIVATE_CHAT.getType())
                    .cmd(PrivateChatCmd.LOGIN_SOCKET)
                    .msgId(IdUtil.getSnowflake(1, 1).nextIdStr())
                    .content(token)
                    .build());
        }

    }

    public void messageReceived(Object data) {

        if (data instanceof MessageWrap) {

            sendBroadcast(data);

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
        login.set(0);
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

        if (readBuffer.capacity() > readBufferSize) {
            readBuffer = ByteBuffer.allocate(readBufferSize);
        }

        sendBroadcast(CommonConstant.HEART);

    }

    private void sendBroadcast(final Object intent) {

        if (null == intent) {
            return;
        }

        if (intent instanceof MessageWrap) {
            MessageWrap message = (MessageWrap) intent;
            if (StrUtil.isEmpty(message.getCmd())) {
                return;
            }
            List<ICall<?>> callList = callListener.get(getListenKey(message.getType(), message.getCmd()));
            if (null != callList) {
                for (ICall<?> iCall : callList) {
                    listenerExecutor.execute(() -> ((ICall<MessageWrap>) iCall).call(message));

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


    private ICall<MessageWrap> onLoginSuccess = messageWrap -> {
        if (BaseResultCode.OK.getCode().equals(messageWrap.getCode())) {
            login.set(1);
        } else {
            login.set(2);
        }
    };
}
