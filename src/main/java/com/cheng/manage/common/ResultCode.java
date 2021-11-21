package com.cheng.manage.common;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name ResultCode
 * @date 2021/10/30 21:29
 */
public class ResultCode {
    public static final int SUCCESS = 200;

    public static final int FAIL = 500;

    /**
     * token过期或尚未登录
     */
    public static final int TOKEN_EXPIRE = 401;

    /**
     * 权限不足
     */
    public static final  int TOKEN_DENIED = 403;
}
