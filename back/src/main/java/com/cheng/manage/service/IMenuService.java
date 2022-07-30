package com.cheng.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.manage.common.model.Result;
import com.cheng.manage.model.Menu;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 查询所有权限
     * @param queryMenu
     * @return
     */
    List<Menu> getMenuList(Menu queryMenu);

    /**
     * 添加权限
     * @param menu
     * @return
     */
    Result addMenu(Menu menu);

    /**
     * 修改权限
     * @param menu
     * @return
     */
    Result updateMenu(Menu menu);

    /**
     * 删除
     * @param menuId
     */
    Result delMenu(Long menuId);
}
