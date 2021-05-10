package com.gylang.gim.remote.qos;

import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.sys.AckMessage;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.remote.SocketHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gylang
 * data 2021/3/3
 */
@Slf4j
@Data
public class ClientQosAdapterHandler {

    private ClientSenderQosHandler senderQosHandler = new DefaultClientSenderSendQosHandler();
    private ClientReceiveQos2Handler receiveQos2Handler = new DefaultIClientReceiveQos2Handler();

    {
        senderQosHandler.startup();
        receiveQos2Handler.startup();
    }

    public MessageWrap process(MessageWrap message) {


        // qos = 1/2 主发逻辑基本一直 统一处理
        if (ChatTypeEnum.QOS_CLIENT_SEND_ACK == message.getType()) {
            // qos 发送方
            senderQosHandler.handle(message);
            return null;
        }

        // 接受方 qos = 1 响应客户点 ack1 并执行正常业务逻辑
        if (QosConstant.INSURE_ONE_ARRIVE == message.getQos()) {
            AckMessage ackMessage = new AckMessage(message);
            ackMessage.setAck(QosConstant.RECEIVE_ACK1);
            ackMessage.setType(ChatTypeEnum.QOS_SERVER_SEND_ACK);
            SocketHolder.getInstance().writeAndFlush(ackMessage);
            if (log.isDebugEnabled()) {
                log.debug("[qos1 - receiver] : 接收到服务端消息 , 响应服务端ack1");
            }
            return null;

        }

        // qos = 2 接受方 发送的为消息体 进行去重判断

        String msgId = message.getMsgId();
        if (QosConstant.ACCURACY_ONE_ARRIVE == message.getQos()) {

            if (0 == message.getAck()) {
                // 发送的是消息 检验去重

                if (receiveQos2Handler.handle(message)) {
                    // 消息未消费 缓存消息 防止重发
                    if (log.isDebugEnabled()) {
                        log.debug("[qos2 - receiver] : 接收到服务端[消息msgId = {} ,首发]消息 , 响应服务端ack1", msgId);
                    }
                    return message;

                }

            } else if (QosConstant.RECEIVE_ACK2 == message.getAck()) {
                // 接受方 收到的消息为ack2 为服务端ack1的回复 可以直接去掉重发记录
                receiveQos2Handler.remove(msgId);
                if (log.isDebugEnabled()) {
                    log.debug("[qos2 - receiver] : 接受方 收到的消息 [msgId = {}]为ack2 为服务端ack1的回复 可以直接去掉重发记录", msgId);
                }
                return null;
            }


        }


        return message;
    }


}
