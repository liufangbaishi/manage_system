package com.cheng.manage.utils;

import cn.hutool.json.JSONUtil;
import com.cheng.manage.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 获取当前登录用户的信息
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name SecurityUtils
 * @date 2021/12/1 22:23
 */
@Slf4j
@Component
public class SecurityUtils {

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return JSONUtil.toBean(JSONUtil.toJsonStr(principal), User.class);
    }

    /**
     * 设置当前用户信息
     * @param authentication
     */
    public static void setCurrentUser(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
