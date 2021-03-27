package com.gylang.netty.sdk.handler.qos;

import com.gylang.netty.sdk.common.InokeFinished;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.constant.ChatTypeEnum;
import com.gylang.netty.sdk.constant.SystemMessageType;
import com.gylang.netty.sdk.domain.MessageWrap;
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
    private IMessageReceiveQosHandler receiveQosHandler;

    @Override
    public void init(NettyConfiguration configuration) {
        this.senderQosHandler = configuration.getIMessageSenderQosHandler();
        this.receiveQosHandler = configuration.getIMessageReceiveQosHandler();
    }

    @Override
    public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {

        if (ChatTypeEnum.SYSTEM_MESSAGE.getType() != message.getType() || message.isQos()) {

            // 判断是否已经接收到消息

            if (SystemMessageType.QOS_SEND_ACK.equals(message.getCmd())) {
                // 主发 保证可靠- 客户端ack
                senderQosHandler.handle(message, me);
                return InokeFinished.getInstance();
            } else if (SystemMessageType.QOS_RECEIVE_ACK.equals(message.getCmd())) {
                // 接收 保证可靠 - 服务端ack
                receiveQosHandler.handle(message, me);
                return InokeFinished.getInstance();
            } else if (receiveQosHandler.hasReceived(message.getMsgId())) {
                // 业务消息重发 响应客户端
                receiveQosHandler.handle(message, me);
                return null;

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
