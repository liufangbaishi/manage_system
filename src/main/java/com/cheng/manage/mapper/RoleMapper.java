package com.cheng.manage.mapper;

import com.cheng.manage.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cheng.manage.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
public interface RoleMapper extends BaseMapper<Role> {

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
