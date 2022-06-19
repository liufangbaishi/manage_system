package com.cheng.manage.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name RoleMenuMapper
 * @date 2022/6/19 10:59
 */
public interface RoleMenuMapper {
    /**
     * 删除角色权限关系
     * @return
     */
    int deleteByRole(@Param("roleId") Long roleId);

    /**
     * 删除角色权限关系
     * @return
     */
    int deleteByMenu(@Param("menuId") Long menuId);

    /**
     * 添加角色权限关系
     * @return
     */
    int addRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 查询关系
     * @param menuId
     * @return
     */
    List<Long> selectByMenuId(@Param("menuId") Long menuId);
}
