package com.cheng.manage.controller;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.cheng.manage.common.consts.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IUserService;
import com.cheng.manage.utils.SecurityUtils;
import com.cheng.manage.vo.TableList;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "分页查询用户列表")
    @PreAuthorize("hasAuthority('sys:user:list')")
    @RequestMapping(value = "getUserList", method = RequestMethod.POST)
    public Result getUserList(@RequestBody PageParam<User> queryUser) {
        TableList userList = userService.getUserList(queryUser);
        return Result.success(userList);
    }

    @ApiOperation(value = "新增用户")
    @PreAuthorize("hasAuthority('sys:user:add')")
    @RequestMapping(value = "saveUser", method = RequestMethod.POST)
    public Result saveUser(@Validated @RequestBody User user) {
        return userService.addUser(user);
    }

    @ApiOperation(value = "查询用户信息")
    @PreAuthorize("hasAuthority('sys:user:query')")
    @RequestMapping(value = "getUser/{userId}", method = RequestMethod.GET)
    public Result getUser(@PathVariable Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return Result.fail("用户ID不能为空");
        }
        User userById = userService.getById(userId);
        if (Objects.isNull(userById)) {
            return Result.fail("用户不存在");
        } else {
            return Result.success(userById);
        }
    }

    @ApiOperation(value = "修改用户信息")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public Result updateUser(@RequestBody User sysUser) {
        if (ObjectUtil.isNull(sysUser.getUserId())) {
            return Result.fail("用户ID不能为空");
        }
        return userService.updateUser(sysUser);
    }

    @ApiOperation(value = "删除用户信息")
    @PreAuthorize("hasAuthority('sys:user:del')")
    @RequestMapping(value = "delUser/{userIds}", method = RequestMethod.GET)
    public Result deleteUser(@PathVariable Long[] userIds) {
        if (ObjectUtil.isNull(userIds)) {
            return Result.fail("用户ID不能为空");
        }
        if (ArrayUtil.contains(userIds, SecurityUtils.getUserId())) {
            return Result.fail("不能删除当前用户");
        }
        return userService.deleteUser(userIds);
    }

    @ApiOperation(value = "修改用户状态")
    @RequestMapping(value = "changeStatus", method = RequestMethod.POST)
    public Result changeStatus(@RequestBody User user) {
        if (ObjectUtil.isNull(user.getUserId())) {
            return Result.fail("用户ID不能为空");
        }
        if (user.getUserId().equals(SecurityUtils.getUserId())) {
            return Result.fail("不能禁用当前用户");
        }
        return userService.updateUser(user);
    }

    @ApiOperation(value = "重置用户密码")
    @PreAuthorize("hasAuthority('sys:user:resetPwd')")
    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    public Result resetPwd(@RequestBody User user) {
        if (ObjectUtil.isNull(user.getUserId())) {
            return Result.fail("用户ID不能为空");
        }
        userService.resetPwd(user);
        return Result.success("重置密码成功");
    }


    @ApiOperation(value = "查询用户对应的角色信息")
    @RequestMapping(value = "getUserByRole/{userId}", method = RequestMethod.GET)
    public Result getUserByRole(@PathVariable Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return Result.fail("用户id不能为空");
        }
        List<Role> roleList = userService.getUserRoleList(userId);
        return Result.success(roleList);
    }

    @ApiOperation(value = "修改个人信息")
    @RequestMapping(value = "updateSelf", method = RequestMethod.POST)
    public Result updateSelf(@RequestBody User sysUser) {
        if (ObjectUtil.isNull(sysUser.getUserId())) {
            return Result.fail("用户ID不能为空");
        }
        if (StrUtil.isEmpty(sysUser.getPhoneNumber())) {
            return Result.fail("请输入手机号");
        }
        return userService.updateUser(sysUser);
    }

    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "updateUserPwd", method = RequestMethod.POST)
    public Result updateUserPwd(@RequestParam("oldPassword") String oldPassword,
                                @RequestParam("newPassword") String newPassword) {
        if (StrUtil.isEmpty(oldPassword) && StrUtil.isEmpty(newPassword)) {
            return Result.fail("密码不能为空");
        }
        return userService.updateUserPwd(oldPassword, newPassword);
    }

    @ApiOperation(value = "修改头像")
    @RequestMapping(value = "uploadAvatar", method = RequestMethod.POST)
    public Result uploadAvatar(@RequestParam("avatarFile") MultipartFile file) {
        return Result.fail("暂不支持修改");
    }

}
