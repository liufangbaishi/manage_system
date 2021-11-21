package com.cheng.manage.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @ApiOperation(value = "测试")
    @GetMapping("test")
    public String hello() {
        return "hello";
    }
}
