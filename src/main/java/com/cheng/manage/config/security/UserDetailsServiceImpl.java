package com.cheng.manage.config.security;

import com.cheng.manage.model.CurrentUser;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name UserDetailServiceImpl
 * @date 2021/12/4 21:30
 */
@Service
public class UserDetailsServiceImpl {

    @Autowired
    private IAuthService authService;

    /**
     * 相当于实现userDetails接口
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 通过用户名查询用户是否存在
            User user = authService.getUserByUserName(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户名不存在");
            }
            if ("1".equals(user.getStatus())) {
                throw new RuntimeException("用户已被禁用");
            }
            // 如果用户存在，查询用户的权限信息
            return new CurrentUser(user.getUserName(), user.getPassword(), getAuthority(user.getUserId()));
        };
    }

    /**
     * 查询用户权限
     * @param userId
     * @return
     */
    List<GrantedAuthority> getAuthority(Long userId) {
        String authority = authService.getAuthority(userId);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
