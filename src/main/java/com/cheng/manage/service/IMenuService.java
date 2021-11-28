package com.cheng.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.manage.common.Result;
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

    List<Menu> getMenuList(Menu queryMenu);

    Result addMenu(Menu menu);
}
