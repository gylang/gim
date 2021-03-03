package com.gylang.netty.sdk.handler.qos;

import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.constant.MessageType;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 处理消息接收，回复客户端ack，保证服务能接收到消息ack
 *
 * @author gylang
 * data 2021/3/3
 */
@Slf4j
public class DefaultIMessageReceiveQosHandler implements IMessageReceiveQosHandler {


    private final ConcurrentMap<String, Long> receiveMessages = new ConcurrentHashMap<>();
    /** 定时任务扫码间隔 */
    private int checkInterval = 10 * 1000;
    /** 消息超时时间 */
    private int messagesValidTime = 10 * 10 * 1000;
    /** 定时扫码器 */
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);


    private boolean executing = false;


    @Override
    public void handle(MessageWrap messageWrap, IMSession target) {

        // 1. 非qos
        if (!messageWrap.isQos()) {
            return;
        }
        // 2. 如果收到的是客户端的ack2包将将消息删除
        if (MessageType.QOS_RECEIVE_ACK == messageWrap.getType()) {
            receiveMessages.remove(messageWrap.getMsgId());
            return;
        }
        // 3. 接收到客户端的消息 msgId,将消息进行保存，
        // 3.1 如果已经存在，ack1给客户端，
        if (!receiveMessages.containsKey(messageWrap.getMsgId())) {
            addReceived(messageWrap);
        }
        // 修改message ack
        // 3.2 发送消息给客户端，ack
        messageWrap.setType(MessageType.QOS_RECEIVE_ACK);
        target.getSession().writeAndFlush(messageWrap);

    }

    @Override
    public boolean hasReceived(String msgId) {
        return receiveMessages.containsKey(msgId);
    }

    @Override
    public void addReceived(MessageWrap messageWrap) {
        receiveMessages.put(messageWrap.getMsgId(), System.currentTimeMillis() + messagesValidTime);
    }

    @Override
    public void startup() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
                    executing = true;
                    scanReceive();
                    executing = false;
                },
                checkInterval,
                checkInterval,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (null != scheduledExecutorService) {
            scheduledExecutorService.shutdown();
        }
    }


    public static interface ScanReceive {
        /**
         * 清除qos队列
         *
         * @param receiveMessages   接收到的消息
         * @param checkInterval     检查间隔
         * @param messagesValidTime 消息过期时间
         */
        void scan(ConcurrentMap<String, Long> receiveMessages, long checkInterval, long messagesValidTime);
    }

    public void scanReceive() {

        if (log.isDebugEnabled()) {

            log.debug("【QoS接收方】START 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
        }

        //** 遍历清除
        for (Map.Entry<String, Long> entry : receiveMessages.entrySet()) {
            String key = entry.getKey();
            long value = entry.getValue();
            // 删除接收消息表
            long delta = System.currentTimeMillis() - value;
            if (delta >= messagesValidTime) {
                if (log.isDebugEnabled()) {
                    log.debug("【QoS接收方】指纹为" + key + "的包已生存" + delta
                            + "ms(最大允许" + messagesValidTime + "ms), 马上将删除之");
                }
                receiveMessages.remove(key);
            }
        }

        if (log.isDebugEnabled()) {

            log.debug("【QoS接收方】END 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
        }
    }

    @Override
    public void init(NettyConfiguration configuration) {

        Integer customCheckInterval = configuration.getProperties(CHECK_INTER_VAL_KEY);
        this.checkInterval = (null == customCheckInterval || customCheckInterval <= 0)
                ? this.checkInterval : customCheckInterval;
        Integer customMessagesValidTime = configuration.getProperties(MESSAGES_VALID_TIME_KEY);
        this.messagesValidTime = (null == customMessagesValidTime || customMessagesValidTime <= 0)
                ? this.messagesValidTime : customMessagesValidTime;
        ScheduledExecutorService customScheduledExecutorService = configuration.getProperties(SCHEDULED_EXECUTOR_SERVICE);
        this.scheduledExecutorService = null == customScheduledExecutorService ? this.scheduledExecutorService : customScheduledExecutorService;

    }
}
