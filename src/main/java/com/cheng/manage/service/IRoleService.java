package com.cheng.manage.service;

import com.cheng.manage.common.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;
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

    TableList getRoleList(PageParam<Role> queryRole);

    List<Role> getAllRoleList(Role role);

    Result addRole(Role role);

    RoleVo getRoleInfo(Long roleId);
}
