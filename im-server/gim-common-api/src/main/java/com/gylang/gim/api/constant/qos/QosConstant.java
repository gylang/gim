package com.gylang.gim.api.constant.qos;

/**
 * @author gylang
 * data 2021/4/8
 */
public interface QosConstant {

    /** 只发送一次 */
    int ONE_AWAY = 0;
    /** 确保有一次送达(不去重) */
    int INSURE_ONE_ARRIVE = 1;
    /** 准确一次送达 */
    int ACCURACY_ONE_ARRIVE = 2;

    /** 主推 第一次发送 (包含消息) */
    int SEND_ACK0 = 0;
    /** 主推 收到客户端发送(重发)ack1回复收到服务端的消息 */
    int SEND_ACK1 = 1;
    /** 主推 回复客户端ack2 让客户端停止重发 */
    int SEND_ACK2 = 2;
    /** 服务端收到客户端消息 (包含消息) 存入消息列表,防止重发 */
    int RECEIVE_ACK0 = 0;
    /** 服务端收到客户端消息后(重发)回复ack 让客户端停止重发 */
    int RECEIVE_ACK1 = 1;
    /** 服务端响应客户端ack2 让客户端停止重发 */
    int RECEIVE_ACK2 = 2;


}
