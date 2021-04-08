package com.gylang.gim.api.dto.msg;

import com.gylang.gim.api.enums.BaseCode;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;

/**
 * @author gylang
 * data 2021/3/6
 */
public class ErrorMessageWrap extends ResponseMessageWrap {

    public static ErrorMessageWrap build(String code, String msg) {

        ErrorMessageWrap messageWrap = new ErrorMessageWrap();
        messageWrap.setCmd(SystemChatCmd.ERROR_MSG);
        messageWrap.setCode(code);
        messageWrap.setContent(msg);
        return messageWrap;
    }

    public static ErrorMessageWrap build(BaseCode baseCode) {
        return build(baseCode.getCode(), baseCode.getMsg());
    }
}
