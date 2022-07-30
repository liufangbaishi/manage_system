package com.cheng.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.manage.common.model.Result;
import com.cheng.manage.mapper.MenuMapper;
import com.cheng.manage.mapper.RoleMenuMapper;
import com.cheng.manage.model.Menu;
import com.cheng.manage.service.IMenuService;
import com.cheng.manage.utils.BuildTree;
import com.cheng.manage.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 查询菜单列表
     * @param queryMenu
     * @return
     */
    @Override
    public List<Menu> getMenuList(Menu queryMenu) {
        // 条件查询
        List<Menu> menuList = new LambdaQueryChainWrapper<>(menuMapper)
                .eq(StrUtil.isNotBlank(queryMenu.getMenuName()), Menu::getMenuName, queryMenu.getMenuName())
                .eq(StrUtil.isNotBlank(queryMenu.getMenuType()), Menu::getMenuType, queryMenu.getMenuType())
                .orderByAsc(Menu::getOrderNum)
                .list();
        // 转换为树形结构
        return BuildTree.buildTree(menuList);
    }

    @Override
    public Result addMenu(Menu menu) {
        // 判重
        Menu checkMenu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getMenuName, menu.getMenuName()));
        if (!Objects.isNull(checkMenu)) {
            return Result.fail("菜单已存在");
        }

        menu.setCreateBy(SecurityUtils.getUserName());
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateBy(SecurityUtils.getUserName());
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.insert(menu);

        return Result.success();
    }

    @Override
    public Result updateMenu(Menu menu) {
        // 判重
        Menu checkMenu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getMenuName, menu.getMenuName()));
        if (!Objects.isNull(checkMenu) && !menu.getMenuName().equals(checkMenu.getMenuName())) {
            return Result.fail("菜单名称不能重复");
        }

        menu.setUpdateBy(SecurityUtils.getUserName());
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.updateById(menu);

        return Result.success();
    }

    @Override
    public Result delMenu(Long menuId) {
        List<Long> menuIds = roleMenuMapper.selectByMenuId(menuId);
        if (CollUtil.isNotEmpty(menuIds)) {
            return Result.fail("删除失败，正在被使用");
        }
        // 删除关系
        roleMenuMapper.deleteByMenu(menuId);
        // 删除菜单
        menuMapper.deleteById(menuId);
        return Result.success();
    }

}
