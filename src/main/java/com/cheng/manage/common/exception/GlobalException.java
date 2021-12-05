package com.cheng.manage.common.exception;

import com.cheng.manage.common.consts.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name GlobalException
 * @date 2021/12/4 23:49
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    /**
     * 实体校验异常捕获
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();
        log.error("实体校验异常：----------------{}", objectError.getDefaultMessage());
        return Result.fail(objectError.getDefaultMessage());
    }

    /**
     * 登录相关异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = LoginException.class)
    public Result handler(LoginException e) {
        return Result.fail(e.getMessage());
    }
}
