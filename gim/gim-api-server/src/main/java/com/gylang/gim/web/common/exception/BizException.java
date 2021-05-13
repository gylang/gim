package com.gylang.gim.web.common.exception;


import com.gylang.gim.api.enums.BaseCode;
import com.gylang.gim.api.enums.BaseResultCode;

/**
 * @author gylang,
 * date 2020/4/14,
 * @version 1.0
 */
public class BizException extends BaseException {

    public BizException() {

        super(BaseResultCode.SYSTEM_ERROR.getCode(), BaseResultCode.SYSTEM_ERROR.getCode());
    }
    public BizException( BaseCode baseCode) {
        super(baseCode.getCode(), baseCode.getMsg());
    }
    public BizException(String code, String msg) {
        super(code, msg);
    }

    public BizException(String message, String code, String msg) {
        super(message, code, msg);
    }

    public BizException(String message, Throwable cause, String code, String msg) {
        super(message, cause, code, msg);
    }

    public BizException(Throwable cause, String code, String msg) {
        super(cause, code, msg);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace, code, msg);
    }
}
