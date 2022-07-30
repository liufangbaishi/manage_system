package com.cheng.manage.common.exception;

import cn.hutool.http.HttpStatus;
import com.cheng.manage.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
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
public class GlobalExceptionHandler {

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

    /**
     * 权限不足异常
     * @param e
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result handler(AccessDeniedException e) throws AccessDeniedException {
        // 全局异常捕获先处理，会使AccessDeninedHandler失效，抛出去，才可以按原来的执行
        throw e;
    }

    /**
     * 其他异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.error("运行时异常：----------------{}", e.getMessage());
        return Result.fail(HttpStatus.HTTP_BAD_REQUEST, e.getMessage());
    }
}
