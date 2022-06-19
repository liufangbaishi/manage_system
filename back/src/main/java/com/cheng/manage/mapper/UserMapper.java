package com.cheng.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cheng.manage.model.User;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取用户列表
     * @param userParam
     * @return
     */
    List<User> getUserList(User userParam);

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    int updateUser(User sysUser);

    /**
     * 删除用户
     * @param userIds
     */
    int deleteUserByIds(Long[] userIds);
}
