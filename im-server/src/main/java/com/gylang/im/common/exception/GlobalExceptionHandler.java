package com.gylang.im.common.exception;

import com.gylang.im.common.dto.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author gylang
 * data 2021/3/4
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public CommonResult<Boolean> baseException(BaseException baseException) {

        return CommonResult.fail(baseException.getCode(), baseException.getMsg());
    }
}