package com.gylang.gim.api.domain.message.sys;

import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.common.ResponseMessage;
import com.gylang.gim.api.enums.ChatTypeEnum;

/**
 * @author gylang
 * data 2021/3/27
 */
public class AckMessage extends ResponseMessage {


    public AckMessage() {
        setType(ChatTypeEnum.SYSTEM_MESSAGE.getType());
        setCmd(SystemChatCmd.QOS_RECEIVE_ACK);
    }

    public AckMessage(MessageWrap message) {
        setClientMsgId(message.getClientMsgId());
        setMsgId(message.getMsgId());
        setType(ChatTypeEnum.SYSTEM_MESSAGE.getType());
        setCmd(SystemChatCmd.QOS_RECEIVE_ACK);
    }
}
