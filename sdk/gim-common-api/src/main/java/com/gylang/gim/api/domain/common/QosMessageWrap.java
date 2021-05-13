package com.gylang.gim.api.domain.common;

import lombok.Data;

/**
 * @author gylang
 * data 2021/5/10
 */
@Data
public class QosMessageWrap {

    /** 消息体 */
    private MessageWrap messageWrap;

    /** 下次发送时间 */
    private long nextTme;

    /** 发送次数 */
    private int sendNum;

    public QosMessageWrap(MessageWrap messageWrap, long nextTime) {
        this.messageWrap = messageWrap;
        this.nextTme = nextTime;
    }

    public String  getReceive() {
        return messageWrap.getReceive();
    }

    public int getQos() {
        return messageWrap.getQos();
    }

    public String getMsgId() {
        return messageWrap.getMsgId();
    }

    public boolean isOfflineMsgEvent() {
        return messageWrap.isOfflineMsgEvent();
    }

    public String getClientMsgId() {
        return messageWrap.getClientMsgId();
    }
}
