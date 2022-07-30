package com.cheng.manage.config.security.response;

import cn.hutool.json.JSONUtil;
import com.cheng.manage.common.model.Result;
import com.cheng.manage.common.consts.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 权限不足异常返回
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name AccessDeniedHandlerImpl
 * @date 2021/12/2 00:04
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset:UTF-8");
        // 返回权限不足的提示
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        Result authenticationResult = Result.fail(ResultCode.TOKEN_DENIED, "权限不足，请联系管理员");
        byte[] byteResult = JSONUtil.toJsonStr(authenticationResult).getBytes(StandardCharsets.UTF_8);
        outputStream.write(byteResult);
        // 关闭流 收尾
        outputStream.flush();
        outputStream.close();
    }
}
