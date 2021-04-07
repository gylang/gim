package com.gylang.gim.web.common.exception;


import com.gylang.gim.api.enums.BaseCode;

/**
 * 系统异常
 * @author gylang,
 * date 2020/5/2,
 * @version 1.0
 */
public class SysException extends BaseException {
    public SysException(String code, String msg) {
        super(code, msg);
    }
    public SysException( BaseCode baseCode) {
        super(baseCode.getCode(), baseCode.getMsg());
    }
    public SysException(String message, String code, String msg) {
        super(message, code, msg);
    }

    public SysException(String message, Throwable cause, String code, String msg) {
        super(message, cause, code, msg);
    }

    public SysException(Throwable cause, String code, String msg) {
        super(cause, code, msg);
    }

    public SysException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace, code, msg);
    }
}
