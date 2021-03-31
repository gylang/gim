package com.gylang.netty.client.constant.system;

/**
 * @author gylang
 * data 2020/11/25
 * @version v0.0.1
 */
public interface SystemMessageType {

    /** qos 服务端重发保证消息可达 - 收到客户端ack包 */
    String QOS_SEND_ACK = "-1";
    /** qos 监听客户端发送的消息是否收到 - 收到客户端ack确认删除相应监听列表 */
    String QOS_RECEIVE_ACK = "-2";
    /** 通知类信息 */
    String NOTIFY = "1";
    /** 错误消息、异常消息 */
    String ERROR_MSG = "3";
    /** 心跳 */
    String HEART = "0";


}
