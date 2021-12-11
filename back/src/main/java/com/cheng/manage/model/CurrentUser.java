package com.cheng.manage.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name CurrentUser
 * @date 2021/12/4 21:38
 */
@Getter
@Setter
public class CurrentUser extends User {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    public CurrentUser(Long userId, String userName, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userName, password, authorities);
        this.userId = userId;
    }
}

