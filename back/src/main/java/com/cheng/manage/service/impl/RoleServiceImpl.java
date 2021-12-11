package com.cheng.manage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.manage.common.consts.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.mapper.MenuMapper;
import com.cheng.manage.mapper.RoleMapper;
import com.cheng.manage.model.Menu;
import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IRoleService;
import com.cheng.manage.utils.BuildTree;
import com.cheng.manage.vo.RoleVo;
import com.cheng.manage.vo.TableList;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 查询所有角色 分页
     * @param queryRole
     * @return
     */
    @Override
    public TableList getRoleList(PageParam<Role> queryRole) {
        PageInfo<Role> pageRoleList = PageHelper
                .startPage(queryRole.getPageNum(), queryRole.getPageSize())
                .doSelectPageInfo(() -> getAllRoleList(queryRole.getParam()));

        return TableList.builder().total(pageRoleList.getTotal()).list(pageRoleList.getList()).build();
    }

    /**
     * 查询所有角色 不分页
     * @param role
     * @return
     */
    @Override
    public List<Role> getAllRoleList(Role role) {
        return new LambdaQueryChainWrapper<>(roleMapper)
                .eq(Role::getDelFlag, 0)
                .eq(StrUtil.isNotBlank(role.getRoleKey()), Role::getRoleKey, role.getRoleKey())
                .eq(StrUtil.isNotBlank(role.getRoleName()), Role::getRoleName, role.getRoleName())
                .list();
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @Override
    public Result addRole(Role role) {
        // 判重
        Role checkRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getRoleKey, role.getRoleKey())
                .or()
                .eq(Role::getRoleName, role.getRoleName()));
        if (!Objects.isNull(checkRole)) {
            return Result.fail("角色已存在");
        }
        role.setCreateBy("admin");
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateBy("admin");
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insert(role);

        return Result.success("新增角色成功");
    }

    /**
     * 查询角色的详细信息 角色名称-角色对应的权限
     * @param roleId
     * @return
     */
    @Override
    public RoleVo getRoleInfo(Long roleId) {
        // 查询角色信息
        Role roleById = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleId, roleId));
        if (ObjectUtil.isNull(roleById)) {
            return null;
        }
        // 查询角色对应的权限
        List<Menu> menuList = menuMapper.selectMenuByRoleId(roleId);
        // 构造为树结结构
        List<Menu> treeMenus = BuildTree.buildTree(menuList);
        // 构造返回结果
        RoleVo resultRole = new RoleVo();
        BeanUtils.copyProperties(roleById, resultRole);
        resultRole.setMenuList(treeMenus);
        return resultRole;
    }

    /**
     * 查询拥有该角色的所有用户
     * @param roleId
     * @return
     */
    @Override
    public List<User> getUserByRoleId(Long roleId) {
        return roleMapper.selectUserByRoleId(roleId);
    }
}