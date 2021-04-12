package com.gylang.gim.api.domain.message.reply;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.common.ResponseMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;

/**
 * @author gylang
 * data 2021/4/8
 */
public class ReplyMessage extends ResponseMessage {

    private ReplyMessage() {
        setType(ChatTypeEnum.REPLY_CHAT.getType());
    }

    public static ReplyMessage success(MessageWrap msg) {

        return success(msg, "成功");
    }
    public static ReplyMessage success(MessageWrap messageWrap, String msg) {
        return reply(messageWrap, BaseResultCode.OK.getCode(), msg);
    }

    public static ReplyMessage reply(MessageWrap messageWrap, String code, String msg) {
        ReplyMessage message = new ReplyMessage();
        message.setClientMsgId(messageWrap.getClientMsgId());
        message.setMsgId(messageWrap.getMsgId());
        message.setCmd(messageWrap.getCmd());
        message.setCode(code);
        message.setMsg(msg);
        return message;
    }

    public static ReplyMessage reply(MessageWrap messageWrap, BaseResultCode resultCode) {

        return reply(messageWrap, resultCode.getCode(), resultCode.getMsg());
    }

}
