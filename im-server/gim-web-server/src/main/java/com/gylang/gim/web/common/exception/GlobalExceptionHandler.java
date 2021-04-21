package com.gylang.gim.web.common.exception;


import com.gylang.gim.api.domain.common.CommonResult;
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
