package com.gylang.netty.sdk.handler.qos;

import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.sys.AckMessage;
import com.gylang.netty.sdk.common.InokeFinished;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author gylang
 * data 2021/3/3
 */
public class QosAdapterHandler implements BizRequestAdapter<MessageWrap> {

    private IMessageSenderQosHandler senderQosHandler;
    private IMessageReceiveQosHandler receiveQos2Handler;

    @Override
    public void init(NettyConfiguration configuration) {
        this.senderQosHandler = configuration.getIMessageSenderQosHandler();
        this.receiveQos2Handler = configuration.getIMessageReceiveQosHandler();
    }

    @Override
    public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {


        // qos = 1/2 主发逻辑基本一直 统一处理
        if (SystemChatCmd.QOS_SEND_ACK.equals(message.getCmd())) {
            // qos 发送方
            senderQosHandler.handle(message, me);
            return InokeFinished.getInstance();
        }

        // 接受方 qos = 1 响应客户点 ack1 并执行正常业务逻辑
        if (QosConstant.INSURE_ONE_ARRIVE == message.getQos()) {
            AckMessage ackMessage = new AckMessage(message);
            ackMessage.setAck(1);
            me.getSession()
                    .writeAndFlush(ackMessage);
            return null;

        }

        // qos = 2 接受方 发送的为消息体 进行去重判断
        String account = me.getAccount();
        String clientMsgId = message.getClientMsgId();
        if (QosConstant.ACCURACY_ONE_ARRIVE == message.getQos()) {

            if (0 == message.getAck()) {
                // 发送的是消息 检验去重

                if (receiveQos2Handler.hasReceived(account, clientMsgId)) {
                    // 消息已经接受 响应ack1
                    AckMessage ackMessage = new AckMessage(message);
                    ackMessage.setAck(1);
                    me.getSession()
                            .writeAndFlush(ackMessage);
                    return InokeFinished.getInstance();
                } else {
                    // 消息未消费 缓存消息 防止重发
                    receiveQos2Handler.handle(message, me);
                    return null;
                }

            } else if (2 == message.getAck()) {
                // 接受方 收到的消息为ack2 为服务端ack1的回复 可以直接去掉重发记录
                receiveQos2Handler.remove(account, clientMsgId);
                return InokeFinished.finish();
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
