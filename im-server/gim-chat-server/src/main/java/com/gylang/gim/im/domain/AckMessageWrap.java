package com.gylang.gim.im.domain;

import com.gylang.netty.sdk.constant.ChatTypeEnum;
import com.gylang.netty.sdk.constant.SystemMessageType;
import com.gylang.netty.sdk.domain.MessageWrap;

/**
 * @author gylang
 * data 2021/3/27
 */
public class AckMessageWrap extends MessageWrap {

    public AckMessageWrap(MessageWrap message) {
        setMsgId(message.getMsgId());
        setType(ChatTypeEnum.SYSTEM_MESSAGE.getType());
        setCmd(SystemMessageType.QOS_RECEIVE_ACK);
    }
}
