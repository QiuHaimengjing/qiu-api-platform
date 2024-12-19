package com.qiu.api.exception;

import com.qiu.api.common.BaseResponse;
import com.qiu.api.common.Result;
import com.qiu.api.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 全局异常处理器
 * @className: GlobalExceptionHandler.java
 * @author: qiu
 * @createTime: 2023/12/16 18:52
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.error("businessException{}", e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return Result.error(ErrorCode.SYSTEM_ERROR);
    }
}
