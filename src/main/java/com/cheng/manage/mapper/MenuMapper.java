package com.cheng.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cheng.manage.model.Menu;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色查询角色对应的权限
     * @param roleId
     * @return
     */
    List<Menu> selectMenuByRoleId(Long roleId);

    /**
     * 根据用户id查询用户的权限
     * @param userId
     * @return
     */
    List<Menu> selectUserMenuList(Long userId);
}
