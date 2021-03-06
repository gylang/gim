package com.gylang.netty.sdk.constant;

/**
 * @author gylang
 * data 2020/11/25
 * @version v0.0.1
 */
public interface MessageType {

    /** qos 服务端重发保证消息可达 - 收到客户端ack包 */
    byte QOS_SEND_ACK = -1;
    /** qos 监听客户端发送的消息是否收到 - 收到客户端ack确认删除相应监听列表 */
    byte QOS_RECEIVE_ACK = -2;
    /** 通知类信息 */
    byte NOTIFY = 1;
    /** 业务类消息 */
    byte BIZ_MSG = 2;
    /** 错误消息、异常消息 */
    byte ERROR_MSG = 3;


}
