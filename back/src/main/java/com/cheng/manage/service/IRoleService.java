package com.cheng.manage.service;

import com.cheng.manage.common.consts.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.manage.model.User;
import com.cheng.manage.vo.RoleVo;
import com.cheng.manage.vo.TableList;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
public interface IRoleService extends IService<Role> {

    /**
     * 查询所有角色 分页
     * @param queryRole
     * @return
     */
    TableList getRoleList(PageParam<Role> queryRole);

    /**
     * 查询所有角色 不分页
     * @param role
     * @return
     */
    List<Role> getAllRoleList(Role role);

    /**
     * 添加角色
     * @param role
     * @return
     */
    Result addRole(Role role);

    /**
     * 查询角色的详细信息 角色名称-角色对应的权限
     * @param roleId
     * @return
     */
    RoleVo getRoleInfo(Long roleId);

    /**
     * 修改角色
     * @param roleVo
     * @return
     */
    Result updateRole(RoleVo roleVo);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    Result delRole(Long roleId);

    /**
     * 查询拥有该角色的所有用户
     * @param roleId
     * @return
     */
    List<User> getUserByRoleId(Long roleId);
}
