package com.cheng.manage.common.exception;

import com.cheng.manage.common.consts.Result;
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
@RestControllerAdvice
public class GlobalException {

//    @ExceptionHandler(AccessDeniedException.class)
//    public Result handleAccessDeniedException(){
//        return Result.fail(ResultCode.TOKEN_DENIED, "无权限访问该接口");
//    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRunTimeException(RuntimeException e) {
        return Result.fail(e.getMessage());
    }
}
