package com.cheng.manage.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.cheng.manage.common.consts.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IUserService;
import com.cheng.manage.vo.TableList;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "getUserList", method = RequestMethod.POST)
    public Result getUserList(@RequestBody PageParam<User> queryUser) {
        TableList userList = userService.getUserList(queryUser);
        return Result.success(userList);
    }

    @ApiOperation(value = "新增用户")
    @RequestMapping(value = "saveUser", method = RequestMethod.POST)
    public Result saveUser(@RequestBody User user) {
        if (StrUtil.isEmpty(user.getUserName())) {
            return Result.fail("用户名不能为空");
        }
        if (StrUtil.isEmpty(user.getNickName())) {
            return Result.fail("昵称不能为空");
        }
        return userService.addUser(user);
    }

    @ApiOperation(value = "查询用户信息")
    @RequestMapping(value = "getUser/{userId}", method = RequestMethod.GET)
    public Result getUser(@PathVariable Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return Result.fail("用户id不能为空");
        }
        User userById = userService.getById(userId);
        if (Objects.isNull(userById)) {
            return Result.fail("用户不存在");
        } else {
            return Result.success(userById);
        }
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

}
