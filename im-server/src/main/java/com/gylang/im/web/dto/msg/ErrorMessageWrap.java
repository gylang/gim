package com.gylang.im.web.dto.msg;

import com.gylang.im.common.enums.BaseCode;
import com.gylang.netty.sdk.constant.SystemMessageType;

/**
 * @author gylang
 * data 2021/3/6
 */
public class ErrorMessageWrap extends ResponseMessageWrap {

    public static ErrorMessageWrap build(String code, String msg) {

        ErrorMessageWrap messageWrap = new ErrorMessageWrap();
        messageWrap.setCmd(SystemMessageType.ERROR_MSG);
        messageWrap.setCode(code);
        messageWrap.setContent(msg);
        return messageWrap;
    }

    public static ErrorMessageWrap build(BaseCode baseCode) {
        return build(baseCode.getCode(), baseCode.getMsg());
    }
}
