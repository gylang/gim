package com.gylang.netty.sdk.handler.qos;

import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.constant.EventTypeConst;
import com.gylang.netty.sdk.constant.MessageType;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 处理消息接收，回复客户端ack，保证服务能接收到消息ack
 *
 * @author gylang
 * data 2021/3/3
 */
@Slf4j
public class DefaultIMessageSendQosHandler implements IMessageSenderQosHandler {

    private final ConcurrentMap<String, Long> receiveMessages = new ConcurrentHashMap<>();
    private ConcurrentSkipListMap<String, MessageWrap> sentMessages = new ConcurrentSkipListMap<>();


    /** 定时任务扫码间隔 */
    private int checkInterval = 10 * 1000;
    /** 消息超时时间 */
    private int messagesValidTime = 10 * 10 * 1000;
    /** 定时扫码器 */
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.AbortPolicy());


    private boolean executing = false;

    private IMSessionRepository imSessionRepository;

    private EventProvider eventProvider;

    @Override
    public void handle(MessageWrap messageWrap, IMSession target) {

        // 1. 非qos
        if (!messageWrap.isQos()) {
            return;
        }
        // 2. 如果收到的是客户端的ack包将将消息删除
        if (MessageType.QOS_SEND_ACK == messageWrap.getType()) {
            receiveMessages.remove(messageWrap.getMsgId());
            return;
        }
        // 3. 接收到客户端的消息 msgId,将消息进行保存，
        // 3.1 如果已经存在，ack1给客户端，
        if (!receiveMessages.containsKey(messageWrap.getMsgId())) {
            addReceived(messageWrap);
        }

    }

    @Override
    public boolean hasReceived(String msgId) {
        return receiveMessages.containsKey(msgId);
    }

    @Override
    public void addReceived(MessageWrap messageWrap) {
        receiveMessages.put(messageWrap.getMsgId(), System.currentTimeMillis() + messagesValidTime);
        sentMessages.put(messageWrap.getMsgId(), messageWrap);
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


    private void scanReceive() {

        /**
         * 清除qos队列
         *
         * @param receiveMessages   接收到的消息
         * @param checkInterval     检查间隔
         * @param messagesValidTime 消息过期时间
         */
        if (log.isDebugEnabled()) {

            log.debug("【QoS发送方】START 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
        }

        //** 遍历清除
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> iterator = receiveMessages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            String key = entry.getKey();
            long expire = entry.getValue();
            // 删除接收消息表
            if (now >= expire) {
                if (log.isDebugEnabled()) {
                    log.debug("【QoS发送方】指纹为" + key + "的包已生存" + messagesValidTime
                            + "ms(最大允许" + messagesValidTime + "ms), 马上将删除之");
                    iterator.remove();

                }

            } else {
                //消息重发
                MessageWrap messageWrap = sentMessages.get(key);
                IMSession imSession = imSessionRepository.find(messageWrap.getTargetId());
                if (null != imSession && imSession.isConnected()) {

                    imSession.getSession().writeAndFlush(messageWrap);
                    messageWrap.setRetryNum(messageWrap.getRetryNum() + 1);
                } else {
                    // 用户离线
                    eventProvider.sendEvent(EventTypeConst.OFFLINE_MSG_EVENT, messageWrap);
                    iterator.remove();
                }
            }
        }

        if (log.isDebugEnabled()) {

            log.debug("【QoS发送方】END 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
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
        this.eventProvider = configuration.getEventProvider();
        this.imSessionRepository = configuration.getSessionRepository();
    }
}
