package com.cheng.manage.config.security;

import cn.hutool.core.util.StrUtil;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IAuthService;
import com.cheng.manage.utils.JwtUtils;
import com.cheng.manage.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截校验token
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name JwtAuthencationFilter
 * @date 2021/12/2 00:08
 */
@Slf4j
@Component
public class JwtAuthencationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IAuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token
        String authToken = jwtUtils.getToken(httpServletRequest);
        if (StrUtil.isNotEmpty(authToken)) {
            // 从token中获取用户名
            String currentUserName = jwtUtils.getCurrentUserName(authToken);
            if (StrUtil.isNotEmpty(currentUserName)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 获取用户信息设置到上下文中
                User currentUser = authService.getUserByUserName(currentUserName);
                String authority = authService.getAuthority(currentUser.getUserId());
                // 校验jwt是否有效
                if (jwtUtils.validateToken(authToken)) {
                    SecurityUtils.setCurrentUser(new UsernamePasswordAuthenticationToken(currentUser,
                            null, AuthorityUtils.commaSeparatedStringToAuthorityList(authority)));
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
