package com.cheng.manage.utils;

import com.cheng.manage.model.CurrentUser;
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

    public static CurrentUser getCurrentUser() {
        return (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static void setCurrentUser(Authentication authentication) {
        log.info(String.valueOf(authentication));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
