package com.cheng.manage.controller;


import cn.hutool.core.util.ObjectUtil;
import com.cheng.manage.common.model.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IRoleService;
import com.cheng.manage.vo.RoleVo;
import com.cheng.manage.vo.TableList;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "分页查询角色列表")
    @PreAuthorize("hasAuthority('sys:role:list')")
    @RequestMapping(value = "getRoleList", method = RequestMethod.POST)
    public Result getRoleList(@RequestBody PageParam<Role> queryRole) {
        TableList roleList = roleService.getRoleList(queryRole);
        return Result.success(roleList);
    }

    @ApiOperation(value = "新增角色")
    @PreAuthorize("hasAuthority('sys:role:add')")
    @RequestMapping(value = "saveRole", method = RequestMethod.POST)
    public Result saveRole(@Validated @RequestBody Role role) {
        return roleService.addRole(role);
    }

    @ApiOperation(value = "查询角色信息")
    @PreAuthorize("hasAuthority('sys:role:query')")
    @RequestMapping(value = "getRole/{roleId}", method = RequestMethod.GET)
    public Result getRole(@PathVariable Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return Result.fail("角色id不能为空");
        }
        RoleVo roleVo = roleService.getRoleInfo(roleId);
        if (Objects.isNull(roleVo)) {
            return Result.fail("角色不存在");
        } else {
            return Result.success(roleVo);
        }
    }

    @ApiOperation(value = "修改角色和权限")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @RequestMapping(value = "updateRole", method = RequestMethod.POST)
    public Result updateRole(@RequestBody RoleVo roleVo) {
        if (ObjectUtil.isNull(roleVo.getRoleId())) {
            return Result.fail("角色id不能为空");
        }
        return Result.success(roleService.updateRole(roleVo));
    }

    @ApiOperation(value = "删除角色")
    @PreAuthorize("hasAuthority('sys:role:del')")
    @RequestMapping(value = "delRole", method = RequestMethod.POST)
    public Result delRole(@RequestParam("roleId") Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return Result.fail("角色id不能为空");
        }
        return Result.success(roleService.delRole(roleId));
    }


    @ApiOperation(value = "查询角色对应的用户信息")
    @RequestMapping(value = "getRoleByUser/{roleId}", method = RequestMethod.GET)
    public Result getRoleByUser(@PathVariable Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return Result.fail("角色id不能为空");
        }
        List<User> userList = roleService.getUserByRoleId(roleId);
        return Result.success(userList);
    }
}
