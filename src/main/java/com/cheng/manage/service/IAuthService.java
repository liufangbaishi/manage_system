package com.cheng.manage.service;

import com.cheng.manage.dto.LoginParam;
import com.cheng.manage.model.User;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name IAuthService
 * @date 2021/12/1 23:54
 */
public interface IAuthService {
    /**
     * 通过用户名查询用户信息
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 通过用户ID查询用户的权限和角色
     * @param userId
     * @return
     */
    String getAuthority(Long userId);

    /**
     * 登录
     * @param loginParam
     * @return
     */
    String login(LoginParam loginParam);
}
