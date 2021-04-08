package com.gylang.gim.remote;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.CommonConstant;
import com.gylang.gim.api.constant.cmd.PrivateChatCmd;
import com.gylang.gim.api.constant.cmd.AdminChatCmd;
import com.gylang.gim.api.domain.MessageWrap;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.remote.call.GimCallBack;
import com.gylang.gim.remote.coder.ClientMessageDecoder;
import com.gylang.gim.remote.coder.ClientMessageEncoder;
import com.gylang.gim.remote.domain.AdminUser;
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
    private String token;
    private AtomicInteger login = new AtomicInteger(0);


    private String loginContent;
    /**
     * 定时任务扫码间隔
     */
    private int checkInterval = 5;
    /**
     * 扫码最低时间间隔
     */
    private int messagesValidTime = 2 * 1000;
    private Map<String, List<GimCallBack<MessageWrap>>> callListener = new HashMap<>();

    /** 消息回调 */
    private Map<String, GimCallBack<MessageWrap>> sendCallBack = new ConcurrentHashMap<>();

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

    /**
     * 连接socket
     *
     * @param ip          ip
     * @param port        端口
     * @param gimCallBack 回调
     */
    public void connect(String ip, Integer port,
                        String username, String password, GimCallBack<String> gimCallBack) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPassword(password);
        loginContent = JSON.toJSONString(adminUser);
        bossExecutor.execute(() -> {
            while (open) {
                try {
                    doConnect(ip, port, gimCallBack);
                    log.info("尝试重连");
                } catch (IOException e) {
                    e.printStackTrace();
                    login.set(0);
                }
            }
        });

    }

    /**
     * 连接socket
     *
     * @param ip          ip
     * @param port        端口
     * @param gimCallBack 回调
     */
    private void doConnect(String ip, Integer port, GimCallBack<String> gimCallBack) throws IOException {
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
                TimeUnit.SECONDS);

        gimCallBack.call("1");
        // 开始读取来自服务端的消息，先读取3个字节的消息头
        while (socketChannel.read(headerBuffer) > 0) {
            handleSocketReadEvent();
        }
    }

    /**
     * 发送心跳
     */
    private void sendHeart() {
        if (socketChannel.isOpen()) {
            log.info("[发送心跳] : 连接正常");
            if (0 == login.get()) {
                login();
            }
            send(CommonConstant.HEART);
        } else {

            log.info("[发送心跳] : 连接异常");
        }
    }

    /**
     * 处理接受到的消息
     */
    private void handleSocketReadEvent() throws IOException {

        // 解码
        Object message = messageDecoder.doDecode(headerBuffer, socketChannel);

        if (CommonConstant.HEART == message) {
            send(CommonConstant.HEART);
            return;
        }

        // 广播
        if (message instanceof MessageWrap) {

            sendBroadcast(message);

        }

    }

    /**
     * 发送消息
     *
     * @param body 消息体
     */
    public void send(final MessageWrap body) {
        if (log.isDebugEnabled()) {
            log.debug("发送消息 : {}", body);
        }
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
                }  // messageSent(body);

            }
        });
    }

    /**
     * 带发送消息
     *
     * @param body        消息体
     * @param gimCallBack 回调信息
     */
    public void sendAndCallBack(final MessageWrap body, GimCallBack<MessageWrap> gimCallBack) {

        send(body);
        sendCallBack.put(body.getClientMsgId(), gimCallBack);
    }


    public void login() {

        if (StrUtil.isNotEmpty(loginContent)) {


            send(MessageWrap.builder()
                    .type(ChatTypeEnum.SYSTEM_MESSAGE.getType())
                    .cmd(AdminChatCmd.REMOTE_LOGIN)
                    .clientMsgId(IdUtil.getSnowflake(1, 1).nextIdStr())
                    .content(loginContent)
                    .build());
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
            log.info(ignore.getMessage());
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
            // 发送消息到监听队列
            List<GimCallBack<MessageWrap>> gimCallBackList = callListener.get(getListenKey(message.getType(), message.getCmd()));
            if (null != gimCallBackList) {
                for (GimCallBack<MessageWrap> gimCallBack : gimCallBackList) {
                    listenerExecutor.execute(() -> gimCallBack.call(message));

                }
            }
            // 判断是否有直接回调消息
            if (null != message.getClientMsgId()) {
                GimCallBack<MessageWrap> gimCallBack = sendCallBack.get(message.getClientMsgId());
                if (null != gimCallBack) {
                    gimCallBack.call(message);
                    sendCallBack.remove(message.getClientMsgId());
                }
            }
        }
    }

    public synchronized void bind(int type, String key, GimCallBack<MessageWrap> gimCallBack) {

        String lk = getListenKey(type, key);
        List<GimCallBack<MessageWrap>> gimCallBackList = callListener.computeIfAbsent(lk, k -> new ArrayList<>());
        gimCallBackList.add(gimCallBack);

    }

    public synchronized void unBind(int type, String key, GimCallBack<?> gimCallBack) {

        String lk = getListenKey(type, key);
        List<GimCallBack<MessageWrap>> gimCallBackList = callListener.get(lk);
        if (null != gimCallBackList) {
            gimCallBackList.remove(gimCallBack);
        }

    }

    private String getListenKey(int type, String key) {
        return type + "-" + key;
    }


    private GimCallBack<MessageWrap> onLoginSuccess = messageWrap -> {
        if (BaseResultCode.OK.getCode().equals(messageWrap.getCode())) {
            login.set(1);
        } else {
            login.set(2);
        }
    };
}
