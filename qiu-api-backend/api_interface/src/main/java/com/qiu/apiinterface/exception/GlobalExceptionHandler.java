package com.qiu.apiinterface.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 全局异常处理器
 * @className: GlobalExceptionHandler.java
 * @author: qiu
 * @createTime: 2024/3/23 11:10
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Object runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return "接口服务异常";
    }
}
