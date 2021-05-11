package com.gylang.gim.remote;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.constant.ContentType;
import com.gylang.gim.api.constant.qos.QosConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.remote.call.GimCallBack;
import com.gylang.gim.remote.coder.ClientMessageDecoder;
import com.gylang.gim.remote.coder.ClientMessageEncoder;
import com.gylang.gim.remote.qos.ClientQosAdapterHandler;
import com.gylang.gim.util.MsgIdUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
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
    private MessageWrap loginMsg;
    private AtomicInteger login = new AtomicInteger(0);
    /**
     * 定时任务扫码间隔
     */
    private int checkInterval = 5;
    private int reconnect = 3;
    private int reconnectNum = 1;
    private int rebindNum = 3;
    /**
     * 扫码最低时间间隔
     */
    private int messagesValidTime = 2 * 1000;
    private Map<String, List<GimCallBack<MessageWrap>>> callListener = new HashMap<>();

    private List<GimCallBack<MessageWrap>> globalCallListener = new LinkedList<>();

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
    private ClientQosAdapterHandler clientQosAdapterHandler = new ClientQosAdapterHandler();
    private InetSocketAddress socketAddress;

    /**
     * 连接socket
     *
     * @param ip          ip
     * @param port        端口
     * @param gimCallBack 回调
     */
    public void connect(String ip, Integer port, MessageWrap loginMsg, GimCallBack<String> gimCallBack) {
        this.loginMsg = loginMsg;
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
        socketAddress = new InetSocketAddress(ip, port);
        openConnect();
        // 登录系统
        login.set(2);
        login();
        bind(ChatTypeEnum.REPLY_CHAT, loginMsg.getCmd(), onLoginSuccess);
        // 心跳监测
        scheduledExecutorService.scheduleAtFixedRate(this::sendHeart,
                checkInterval,
                checkInterval,
                TimeUnit.SECONDS);

        // 开始读取来自服务端的消息，先读取3个字节的消息头
        gimCallBack.call("1");
        while (socketChannel.read(headerBuffer) > 0) {
            handleSocketReadEvent();
        }
    }

    private void openConnect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        socketChannel.socket().setTcpNoDelay(true);
        socketChannel.socket().setKeepAlive(true);
        socketChannel.socket().setReceiveBufferSize(readBufferSize);
        socketChannel.socket().setSendBufferSize(writeBufferSize);
        socketChannel.socket().connect(socketAddress, connectTimeOut);
    }

    /**
     * 发送心跳
     */
    private void sendHeart() {
        if (socketChannel.isOpen()) {
            log.info("[发送心跳] : 连接正常");
            if (0 == login.get()) {
                login.set(2);
                login();
            }
            send(CommonConstant.HEART);
        } else {
            reconnect = reconnect % reconnectNum;
            if (0 == reconnect) {
                log.info("[发送心跳] : 连接异常, 进行重连");
                try {
                    socketChannel.close();
                    openConnect();
                } catch (IOException e) {
                    log.info("[重连] : 重连失败");
                    login.set(0);
                }
            }
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
            log.info("[接受到服务端消息] : {}", message);
            sendBroadcast(message);

        }

    }

    /**
     * 发送消息
     *
     * @param body 消息体
     */
    public void send(final MessageWrap body) {

        if (ChatTypeEnum.HEART != body.getType() && log.isDebugEnabled()) {
            log.debug("发送消息 : {}", body);
        }
        if (!isConnected()) {
            return;
        }
        MsgIdUtil.increase(body);
        workerExecutor.execute(() -> {
            if (QosConstant.ONE_AWAY != body.getQos()) {
                clientQosAdapterHandler.getSenderQosHandler().addReceived(body);
            }
            writeAndFlush(body);
        });
    }

    public void writeAndFlush(MessageWrap body) {
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
            }

        }
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

        if (null != loginMsg) {

            send(loginMsg);
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

    public void sendBroadcast(final Object msg) {

        if (null == msg) {
            return;
        }

        if (msg instanceof MessageWrap) {
            MessageWrap message = (MessageWrap) msg;
            // 批量消息
            if (ContentType.BATCH.equals(message.getContentType())) {
                String batchMessageStr = message.getContent();
                List<MessageWrap> batchMessage = JSON.parseArray(batchMessageStr, MessageWrap.class);
                for (MessageWrap messageWrap : batchMessage) {
                    sendBroadcast(messageWrap);
                }
                return;
            }

            if (null == clientQosAdapterHandler.process(message)) {
                // qo校验过滤包
                return;
            }


            // 发送消息到监听队列
            List<GimCallBack<MessageWrap>> gimCallBackList = callListener.get(getListenKey(message.getType(), message.getCmd()));
            listenerExecutor.execute(() -> {
               // 全局监听
                for (GimCallBack<MessageWrap> messageWrapGimCallBack : globalCallListener) {
                    messageWrapGimCallBack.call(message);
                }
                // 业务监听
                if (null != gimCallBackList) {
                    for (GimCallBack<MessageWrap> gimCallBack : gimCallBackList) {
                        gimCallBack.call(message);
                    }
                }
            });

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

    public synchronized void globalBind(GimCallBack<MessageWrap> gimCallBack) {

        globalCallListener.add(gimCallBack);

    }

    public synchronized void unGlobalBind(GimCallBack<MessageWrap> gimCallBack) {

        globalCallListener.remove(gimCallBack);

    }

    public synchronized void unBind(int type, String key, GimCallBack<?> gimCallBack) {

        String lk = getListenKey(type, key);
        List<GimCallBack<MessageWrap>> gimCallBackList = callListener.get(lk);
        if (null != gimCallBackList) {
            gimCallBackList.remove(gimCallBack);
        }

    }

    private static String getListenKey(int type, String key) {
        return type + "-" + key;
    }


    private GimCallBack<MessageWrap> onLoginSuccess = messageWrap -> {
        if (BaseResultCode.OK.getCode().equals(messageWrap.getCode())) {
            login.set(1);
        } else {
            rebindNum -= 1;
            if (rebindNum <= 0) {
                login.set(2);
            } else {
                login.set(0);
            }
        }
    };
}
