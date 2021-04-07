package com.gylang.gim.server.domain;

import com.gylang.netty.sdk.constant.ChatTypeEnum;
import com.gylang.netty.sdk.constant.system.SystemMessageType;
import com.gylang.netty.sdk.domain.MessageWrap;

/**
 * @author gylang
 * data 2021/3/27
 */
public class AckMessageWrap extends ResponseMessageWrap {

    public AckMessageWrap(MessageWrap message) {
        setClientMsgId(message.getClientMsgId());
        setMsgId(message.getMsgId());
        setType(ChatTypeEnum.SYSTEM_MESSAGE.getType());
        setCmd(SystemMessageType.QOS_RECEIVE_ACK);
    }
}
