package com.gylang.gim.web.common.exception;


import com.gylang.gim.web.api.enums.BaseCode;

/**
 * @author gylang,
 * date 2020/5/2,
 * @version 1.0
 */
public class UnableProcessException extends BaseException {
    public UnableProcessException(String code, String msg) {
        super(code, msg);
    }

    public UnableProcessException( BaseCode baseCode) {
        super(baseCode.getCode(), baseCode.getMsg());
    }
    public UnableProcessException(String message, String code, String msg) {
        super(message, code, msg);
    }

    public UnableProcessException(String message, Throwable cause, String code, String msg) {
        super(message, cause, code, msg);
    }

    public UnableProcessException(Throwable cause, String code, String msg) {
        super(cause, code, msg);
    }

    public UnableProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace, code, msg);
    }
}
