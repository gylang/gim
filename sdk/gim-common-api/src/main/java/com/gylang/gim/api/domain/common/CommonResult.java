package com.gylang.gim.api.domain.common;

import com.gylang.gim.api.enums.BaseResultCode;
import lombok.Data;

/**
 * @author gylang
 * data 2020/10/28
 * @version v0.0.1
 */
@Data
public class CommonResult<T> {

    private String code;

    private String msg;

    private T data;

    public static <T> CommonResult<T> ok() {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(BaseResultCode.OK.getCode());
        result.setMsg(BaseResultCode.OK.getMsg());
        return result;
    }

    public static <T> CommonResult<T> ok(T t) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(BaseResultCode.OK.getCode());
        result.setMsg(BaseResultCode.OK.getMsg());
        result.setData(t);
        return result;
    }

    public static <T> CommonResult<T> fail(BaseResultCode error) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(error.getCode());
        result.setMsg(error.getMsg());
        return result;
    }

    public static <T> CommonResult<T> fail(String code, String msg) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static CommonResult<Boolean> auto(Boolean code) {
        if (Boolean.TRUE.equals(code)) {
            return ok(code);
        }
        return fail(BaseResultCode.PARAMS_ERROR);
    }
}
