package com.gylang.netty.sdk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一交互信息包装类
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageWrap implements Serializable {

    /** 命令 */
    private String cmd;
    /** 发送者 */
    private String sender;
    /** 消息类型 */
    private int type;
    /** 消息主体 */
    private String content;
    /** 消息体类型 */
    private String contentType;
    /** 消息code */
    private String code;
    /** 接收者 */
    private String receive;
    /** 接收者类型 */
    private byte receiverType;


    /** 消息id */
    private String msgId;
    /** 重试次数 */
    private transient int retryNum;

    /** 是否使用质量服务, ack */
    private boolean qos;

    /** 持久化消息事件 */
    private transient boolean persistenceEvent;

    /** 离线/失败消息事件发送 */
    private transient boolean offlineMsgEvent = true;

    private long timeStamp;


    public MessageWrap copyBasic() {

        MessageWrap message = new MessageWrap();
        message.setCmd(cmd);
        message.setSender(sender);
        message.setType(type);
        message.setContent(content);
        message.setContentType(contentType);
        message.setCode(code);
        message.setReceive(receive);
        message.setReceiverType(receiverType);
        message.setQos(qos);
        message.setPersistenceEvent(persistenceEvent);
        message.setOfflineMsgEvent(offlineMsgEvent);
        message.setTimeStamp(timeStamp);
        return message;

    }





}
