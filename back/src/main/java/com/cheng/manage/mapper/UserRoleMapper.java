package com.cheng.manage.mapper;

import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name UserRoleMapper
 * @date 2021/12/19 19:04
 */
public interface UserRoleMapper {
    /**
     * 删除用户角色关系
     * @return
     */
    int deleteUserRole(@Param("userIds") Long[] userIds);

    /**
     * 添加用户角色关系
     * @return
     */
    int addUserRole();


    /**
     * 查询roleId对应的用户列表
     * @param roleId
     * @return
     */
    List<User> selectUserByRoleId(@Param("roleId") Long roleId);


    /**
     * 获取用户对应的角色列表
     * @param userId
     */
    List<Role> selectUserRoleList(@Param("userId") Long userId);
}
