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
    private String type;
    /** 消息主体 */
    private String content;
    /** 消息code */
    private String code;
    /** 接收者 */
    private String receive;
    /** 接收者类型 */
    private String receiverType;
    /** 消息id */
    private String msgId;
    /** 重试次数 */
    private transient int retryNum;
    /** 是否使用质量服务, ack */
    private boolean qos;
}
