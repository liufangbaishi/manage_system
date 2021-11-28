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

    List<Menu> selectMenuByRoleId(Long roleId);
}
