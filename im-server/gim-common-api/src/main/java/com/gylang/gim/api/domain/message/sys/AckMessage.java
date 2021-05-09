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
        setType(ChatTypeEnum.QOS_CLIENT_SEND_ACK);
    }
    public AckMessage(String cmd) {
        setType(ChatTypeEnum.SYSTEM_MESSAGE);
        setCmd(cmd);
    }

    public AckMessage(MessageWrap message) {
        setMsgId(message.getMsgId());
        setClientMsgId(message.getClientMsgId());
        setQos(message.getQos());
        setType(message.getType());
        setCmd(message.getCmd());
    }
    public AckMessage(int type,MessageWrap message) {
        setClientMsgId(message.getClientMsgId());
        setMsgId(message.getMsgId());
        setQos(message.getQos());
        setType(type);
    }
}
