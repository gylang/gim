package com.gylang.gim.api.domain.message.sys;

import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.ResponseMessage;
import com.gylang.gim.api.enums.BaseCode;
import com.gylang.gim.api.enums.ChatType;

/**
 * @author gylang
 * data 2021/3/6
 */
public class SysErrorMessage extends ResponseMessage {

    public static SysErrorMessage build(String code, String msg) {

        SysErrorMessage messageWrap = new SysErrorMessage();
        messageWrap.setCmd(SystemChatCmd.ERROR_MSG);
        messageWrap.setCode(code);
        messageWrap.setType(ChatType.SYSTEM_MESSAGE);
        messageWrap.setContent(msg);
        return messageWrap;
    }

    public static SysErrorMessage build(BaseCode baseCode) {
        return build(baseCode.getCode(), baseCode.getMsg());
    }
}
