package com.gylang.netty.sdk.handler.qos;

import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.gim.api.domain.common.MessageWrap;
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

    private final ConcurrentMap<String, Long> messageTimeStamp = new ConcurrentHashMap<>();
    private ConcurrentSkipListMap<String, MessageWrap> sentMessages = new ConcurrentSkipListMap<>();


    /**
     * 定时任务扫码间隔
     */
    private int checkInterval = 5 * 1000;
    /**
     * 扫码最低时间间隔
     */
    private int messagesValidTime = 2 * 1000;
    /**
     * 定时扫码器
     */
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.AbortPolicy());


    private boolean executing = false;

    private int reSendNum = 3;

    private IMSessionRepository imSessionRepository;

    private EventProvider eventProvider;

    @Override
    public void handle(MessageWrap message, IMSession target) {

        // 1. 非qos
        if (ChatTypeEnum.SYSTEM_MESSAGE.getType() != message.getType() || !message.isQos()) {
            return;
        }
        // 2. 如果收到的是客户端的ack包将将消息删除
        if (SystemChatCmd.QOS_SEND_ACK.equals(message.getCmd())) {
            messageTimeStamp.remove(message.getMsgId());
            return;
        }
        // 3. 接收到客户端的消息 msgId,将消息进行保存，
        // 3.1 如果已经存在，ack1给客户端，
        if (!messageTimeStamp.containsKey(message.getMsgId())) {
            addReceived(message);
        }

    }

    @Override
    public boolean hasReceived(String msgId) {
        return messageTimeStamp.containsKey(msgId);
    }

    @Override
    public void addReceived(MessageWrap messageWrap) {
        messageTimeStamp.put(messageWrap.getMsgId(), System.currentTimeMillis() + messagesValidTime);
        sentMessages.put(messageWrap.getMsgId(), messageWrap);
    }

    @Override
    public void startup() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
                    if (!executing) {
                        executing = true;
                        scanReceive();
                        executing = false;
                    }
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

            log.debug("【QoS发送方】START 暂存处理线程正在运行中，当前长度" + messageTimeStamp.size() + ".");
        }

        //** 遍历清除
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, MessageWrap>> iterator = sentMessages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MessageWrap> entry = iterator.next();
            String key = entry.getKey();
            MessageWrap msg = entry.getValue();
            // 删除接收消息表
            if (msg.getRetryNum() >= reSendNum) {
                if (log.isDebugEnabled()) {
                    log.debug("【QoS发送方】消息:msgId" + msg.getMsgId() + "的包已生存" + reSendNum
                            + "(最大允许" + reSendNum + "次数), 马上将删除之");
                    iterator.remove();

                }

            } else {
                //消息重发
                long timestamp = messageTimeStamp.get(key);
                if (now < timestamp) {
                    continue;
                }
                IMSession imSession = imSessionRepository.find(msg.getReceive());
                if (null != imSession && imSession.isConnected()) {

                    imSession.getSession().writeAndFlush(timestamp);
                    msg.setRetryNum(msg.getRetryNum() + 1);
                } else {
                    // 用户离线
                    if (msg.isOfflineMsgEvent()) {
                        eventProvider.sendEvent(EventTypeConst.OFFLINE_MSG_EVENT, timestamp);
                    }
                    iterator.remove();
                }
            }
        }

        if (log.isDebugEnabled()) {

            log.debug("【QoS发送方】END 暂存处理线程正在运行中，当前长度" + messageTimeStamp.size() + ".");
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
