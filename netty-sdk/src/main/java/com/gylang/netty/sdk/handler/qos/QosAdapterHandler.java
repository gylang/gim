package com.gylang.netty.sdk.handler.qos;

import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.sys.AckMessage;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.netty.sdk.common.InvokeFinished;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * qos 处理器 支持qos1(保证至少一次可达) qos2(保证只有一次消费) 确保消息可达
 * @author gylang
 * data 2021/3/3
 */
@Slf4j
public class QosAdapterHandler implements BizRequestAdapter {

    private IMessageSenderQosHandler senderQosHandler;
    private IMessageReceiveQosHandler receiveQos2Handler;

    @Override
    public void init(GimGlobalConfiguration configuration) {
        this.senderQosHandler = configuration.getIMessageSenderQosHandler();
        this.receiveQos2Handler = configuration.getIMessageReceiveQosHandler();
    }

    @Override
    public Object process(ChannelHandlerContext ctx, GIMSession me, MessageWrap message) {


        // qos = 1/2 主发逻辑基本一直 统一处理
        if (ChatTypeEnum.QOS_SERVER_SEND_ACK == message.getType()) {
            // qos 发送方
            senderQosHandler.handle(message, me);
            return InvokeFinished.getInstance();
        }

        // 接受方 qos = 1 响应客户点 ack1 并执行正常业务逻辑
        if (QosConstant.INSURE_ONE_ARRIVE == message.getQos()) {
            AckMessage ackMessage = new AckMessage(message);
            ackMessage.setAck(QosConstant.RECEIVE_ACK1);
            ackMessage.setType(ChatTypeEnum.QOS_CLIENT_SEND_ACK);
            me.getSession().writeAndFlush(ackMessage);
            if (log.isDebugEnabled()) {
                log.debug("[qos1 - receiver] : 接收到客户端消息 , 响应客户端ack1");
            }
            return null;

        }

        // qos = 2 接受方 发送的为消息体 进行去重判断
        String account = me.getAccount();
        String clientMsgId = message.getClientMsgId();
        if (QosConstant.ACCURACY_ONE_ARRIVE == message.getQos()) {

            if (0 == message.getAck()) {
                // 发送的是消息 检验去重

                if (receiveQos2Handler.handle(message, me)) {
                    // 消息未消费 缓存消息 防止重发
                    if (log.isDebugEnabled()) {
                        log.debug("[qos2 - receiver] : 接收到客户端[首发]消息 , 响应客户端ack1");
                    }
                    return null;
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("[qos2 - receiver] : 接收到客户端[重发]消息 , 响应客户端ack1");
                    }
                    return InvokeFinished.getInstance();
                }

            } else if (QosConstant.RECEIVE_ACK2 == message.getAck()) {
                // 接受方 收到的消息为ack2 为服务端ack1的回复 可以直接去掉重发记录
                receiveQos2Handler.remove(account, clientMsgId);

                return InvokeFinished.finish();
            }


        }
        return null;
    }

    @Override
    public void register(List<ObjectWrap> processList) {

    }

    @Override
    public Integer order() {
        return 50;
    }
}
