package com.gylang.gim.remote.qos;

import com.gylang.gim.api.domain.common.MessageWrap;

/**
 * 处理收到接受到的消息，发送ack，保证消息已经接受成功
 * 存储消息列表，当用户消息重复的时候对其进行消费判断，假设重发信息，回复ack
 * 当消息进行双向ack之后，清除消息
 *
 * @author gylang
 * data 2021/2/11
 */
public interface ClientReceiveQos2Handler {

    String CHECK_INTER_VAL_KEY = "IMessageReceiveQosHandler.checkInterval";

    String MESSAGES_VALID_TIME_KEY = "IMessageReceiveQosHandler.messagesValidTime";

    String SCAN_RECEIVE_KEY = "IMessageReceiveQosHandler.scanReceive";

    String SCHEDULED_EXECUTOR_SERVICE = "IMessageReceiveQosHandler.scheduledExecutorService";


    /**
     * 处处理消息
     *
     * @param target      接收者
     * @param messageWrap 消息包装类
     * @return
     */
    boolean handle(MessageWrap messageWrap);

    /**
     * 是否已经接收到当前消息
     * @param msgId 消息序列号
     * @return 是否接收
     */
    boolean hasReceived(String msgId);

    void remove(String msgId);

    /**
     * 添加已接收队列
     *
     * @param key 消息id
     * @return
     */
    boolean addReceived(String key, MessageWrap message);

    /**
     * 启动扫码器
     */
    void startup();

    /**
     * 关闭扫码器
     */
    void stop();

}
