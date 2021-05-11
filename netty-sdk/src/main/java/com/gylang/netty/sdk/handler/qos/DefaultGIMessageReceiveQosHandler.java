package com.gylang.netty.sdk.handler.qos;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.sys.AckMessage;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 处理客户端主发消息(服务端消息接收方)，(重发)回复客户端ack，保证服务能接收到消息ack
 *
 * @author gylang
 * data 2021/3/3
 */
@Slf4j
@Setter
public class DefaultGIMessageReceiveQosHandler implements IMessageReceiveQosHandler {


    private final ConcurrentMap<String, MessageWrap> receiveMessages = new ConcurrentHashMap<>();
    /** 定时任务扫码间隔 */
    private int checkInterval = 10 * 1000;
    /** 消息超时时间 */
    private int messagesValidTime = 10 * 10 * 1000;
    /** 定时扫码器 */
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, ThreadFactoryBuilder.create().setNamePrefix("receive-qos-scanner").build());
    private int reSendNum = 3;

    private boolean executing = false;

    private IMSessionRepository sessionRepository;


    @Override
    public boolean handle(MessageWrap message, GIMSession target) {

        // qos2 接受方处理 响应ack1
        // 使用后 uid, 客户端需要bind才能使用
        // 使用nid 确保了当前会话不中断时的qos2能确保不重样, 但是连接断开释放之后消息可以重发,(客户端中断连接后)
        // 仅使用 msgId 需要确保客户端的生成全局唯一id 可以实现可能出现伪造消息/数据量大时客户端生成的clientId一样
        AckMessage ackMessage = new AckMessage(ChatTypeEnum.QOS_CLIENT_SEND_ACK, message);
        ackMessage.setAck(QosConstant.RECEIVE_ACK1);
        boolean add = true;
        MessageWrap messageWrap = new AckMessage(message);
        if (StrUtil.isEmpty(target.getAccount())) {
            // 未登录 降级qos1
            messageWrap.setQos(QosConstant.INSURE_ONE_ARRIVE);
            if (log.isDebugEnabled()) {
                log.debug("[qos 客户端主发] : 用户未登录, qos降级为qos1, clientMsgId = {}", message.getClientMsgId());
            }
        } else {
            add = addReceived(getKey(target.getAccount(), ackMessage.getClientMsgId()), ackMessage);
            if (log.isDebugEnabled()) {
                log.debug("[qos 客户端主发] : 用户登录, qos2, 缓存消息ack1列表, 防止重发, clientMsgId = {}", message.getClientMsgId());
            }
        }
        messageWrap.setAck(QosConstant.RECEIVE_ACK1);
        messageWrap.setType(ChatTypeEnum.QOS_CLIENT_SEND_ACK);
        target.getSession().writeAndFlush(messageWrap);
        return add;
    }

    @Override
    public boolean hasReceived(String senderId, String msgId) {
        return receiveMessages.containsKey(getKey(senderId, msgId));
    }

    @Override
    public void remove(String senderId, String msgId) {

        receiveMessages.remove(getKey(senderId, msgId));
    }

    @Override
    public boolean addReceived(String key, MessageWrap message) {
        if (receiveMessages.containsKey(key)) {
            return false;
        }
        receiveMessages.put(key, message);
        return true;
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


    public void scanReceive() {

        if (log.isDebugEnabled()) {

            log.debug("【QoS接收方】START 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
        }

        //** 遍历清除
        for (Map.Entry<String, MessageWrap> entry : receiveMessages.entrySet()) {
            String key = entry.getKey();
            MessageWrap value = entry.getValue();

            // 删除接收消息表
            int retryNum = value.getRetryNum();
            value.setRetryNum(retryNum + 1);
            if (retryNum >= reSendNum) {
                if (log.isDebugEnabled()) {
                    log.debug("【QoS接收方】指纹为" + key + "的包已生存" + reSendNum
                            + "次(最大允许" + reSendNum + "次), 马上将删除之");
                }
                receiveMessages.remove(key);
            } else {
                // 处理客户端主发回应ack 所有发送的是给发送方
                GIMSession session = sessionRepository.find(value.getSender());
                if (null != session) {
                    session.getSession().writeAndFlush(value);
                }
            }
        }

        if (log.isDebugEnabled()) {

            log.debug("【QoS接收方】END 暂存处理线程正在运行中，当前长度" + receiveMessages.size() + ".");
        }
    }

    @Override
    public void init(GimGlobalConfiguration configuration) {

        Integer customCheckInterval = configuration.getProperties(CHECK_INTER_VAL_KEY);
        this.checkInterval = (null == customCheckInterval || customCheckInterval <= 0)
                ? this.checkInterval : customCheckInterval;
        Integer customMessagesValidTime = configuration.getProperties(MESSAGES_VALID_TIME_KEY);
        this.messagesValidTime = (null == customMessagesValidTime || customMessagesValidTime <= 0)
                ? this.messagesValidTime : customMessagesValidTime;
        ScheduledExecutorService customScheduledExecutorService = configuration.getProperties(SCHEDULED_EXECUTOR_SERVICE);
        this.scheduledExecutorService = null == customScheduledExecutorService ? this.scheduledExecutorService : customScheduledExecutorService;
        this.sessionRepository = configuration.getSessionRepository();

    }

    public String getKey(String senderId, String msgId) {

        return senderId + ":" + msgId;
    }
}
