package com.gylang.gim.server.domain;

import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.enums.BaseCode;
import com.gylang.gim.api.enums.ChatTypeEnum;

/**
 * @author gylang
 * data 2021/3/6
 */
public class ErrorMessageWrap extends ResponseMessageWrap {

    public static ErrorMessageWrap build(String code, String msg) {

        ErrorMessageWrap messageWrap = new ErrorMessageWrap();
        messageWrap.setCmd(SystemChatCmd.ERROR_MSG);
        messageWrap.setCode(code);
        messageWrap.setType(ChatTypeEnum.SYSTEM_MESSAGE.getType());
        messageWrap.setContent(msg);
        return messageWrap;
    }

    public static ErrorMessageWrap build(BaseCode baseCode) {
        return build(baseCode.getCode(), baseCode.getMsg());
    }
}
