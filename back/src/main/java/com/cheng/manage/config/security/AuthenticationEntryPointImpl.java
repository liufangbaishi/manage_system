package com.cheng.manage.config.security;

import cn.hutool.json.JSONUtil;
import com.cheng.manage.common.consts.Result;
import com.cheng.manage.common.consts.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 未登录异常返回
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name AuthenticationEntryPointImpl
 * @date 2021/12/2 00:01
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset:UTF-8");
        // 返回登录提示
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        Result authenticationResult = Result.fail(ResultCode.TOKEN_EXPIRE, "token过期，请重新登录");
        byte[] byteResult = JSONUtil.toJsonStr(authenticationResult).getBytes(StandardCharsets.UTF_8);
        outputStream.write(byteResult);
        // 关闭流 收尾
        outputStream.flush();
        outputStream.close();
    }
}
