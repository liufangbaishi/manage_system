package com.cheng.manage.common.consts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name Result
 * @date 2021/11/21 20:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int code;

    private String message;

    private Object data;

    public static Result success(String message) {
        return new Result(ResultCode.SUCCESS, message, null);
    }

    public static Result success(Object data) {
        return new Result(ResultCode.SUCCESS, "操作成功", data);
    }

    public static Result success(String message, Object data) {
        return new Result(ResultCode.SUCCESS, message, data);
    }

    public static Result fail(String message) {
        return new Result(ResultCode.FAIL, message, null);
    }

    public static Result fail(String message, Object data) {
        return new Result(ResultCode.FAIL, message, data);
    }

    public static Result fail(int code, String message) {
        return new Result(code, message, null);
    }
}
