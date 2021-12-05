package com.cheng.manage.controller;


import com.cheng.manage.common.consts.Result;
import com.cheng.manage.model.Menu;
import com.cheng.manage.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "分页查询菜单列表")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @RequestMapping(value = "getMenuList", method = RequestMethod.POST)
    public Result getMenuList(@RequestBody Menu queryMenu) {
        List<Menu> menuList = menuService.getMenuList(queryMenu);
        return Result.success(menuList);
    }

    @ApiOperation(value = "新增菜单")
    @RequestMapping(value = "saveMenu", method = RequestMethod.POST)
    public Result saveMenu(@Validated @RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    @ApiOperation(value = "查询菜单信息")
    @RequestMapping(value = "getMenu/{menuId}", method = RequestMethod.GET)
    public Result getMenu(@PathVariable Long menuId) {
        Menu menuById = menuService.getById(menuId);
        return Result.success(menuById);
    }

}
