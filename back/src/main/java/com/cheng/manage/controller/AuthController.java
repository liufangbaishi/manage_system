package com.cheng.manage.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.cheng.manage.common.consts.Result;
import com.cheng.manage.dto.LoginParam;
import com.cheng.manage.service.IAuthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录、登出、查询当前登录人的信息(角色、菜单等)
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name AuthController
 * @date 2021/12/1 21:39
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Value("${jwt.tokenHead:Bearer}")
    private String head;

    @ApiOperation(value = "登录")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestBody LoginParam loginParam, HttpServletRequest request) {
        if (StrUtil.isEmpty(loginParam.getUserName()) || StrUtil.isEmpty(loginParam.getPassword())) {
            return Result.fail("用户名或密码不能为空");
        }
        String token = authService.login(loginParam, request);
        return Result.success(MapUtil.builder()
                .put("token", token)
                .put("head", head)
                .build());
    }


    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "getCaptcha", method = RequestMethod.GET)
    public Result getCaptcha() {
        Result result;
        try {
            result = authService.getCaptcha();
        } catch (Exception e) {
            result = Result.fail("生成验证码失败");
        }
        return result;
    }

    @ApiOperation(value = "获取当前登录用户的基本信息")
    @RequestMapping(value = "getCurrentUser", method = RequestMethod.GET)
    public Result getCurrentUser() {
        return authService.getCurrentUser();
    }

    @ApiOperation(value = "获取当前用户的权限信息")
    @RequestMapping(value = "nav", method = RequestMethod.GET)
    public Result getNav() {
        return authService.getCurrentNav();
    }
}
