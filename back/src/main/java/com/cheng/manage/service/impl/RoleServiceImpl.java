package com.cheng.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.manage.common.model.Result;
import com.cheng.manage.common.enums.DelStausEnum;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.mapper.MenuMapper;
import com.cheng.manage.mapper.RoleMapper;
import com.cheng.manage.mapper.RoleMenuMapper;
import com.cheng.manage.mapper.UserRoleMapper;
import com.cheng.manage.model.Menu;
import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IRoleService;
import com.cheng.manage.utils.BuildTree;
import com.cheng.manage.utils.SecurityUtils;
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
import java.util.stream.Collectors;

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

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 查询所有角色 分页
     * @param queryRole
     * @return
     */
    @Override
    public TableList getRoleList(PageParam<Role> queryRole) {
        PageInfo<Role> pageRoleList = PageHelper
                .startPage(queryRole.getPageNum(), queryRole.getPageSize())
                .doSelectPageInfo(() -> getAllRoleList(queryRole.getParams()));

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
                .eq(Role::getDelFlag, DelStausEnum.NORMAL.getCode())
                .eq(StrUtil.isNotBlank(role.getRoleKey()), Role::getRoleKey, role.getRoleKey())
                .eq(StrUtil.isNotBlank(role.getRoleName()), Role::getRoleName, role.getRoleName())
                .orderByAsc(Role::getRoleSort)
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
        role.setCreateBy(SecurityUtils.getUserName());
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateBy(SecurityUtils.getUserName());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insert(role);

        return Result.success();
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

    @Override
    public Result updateRole(RoleVo roleVo) {
        // 判断重复
        Role checkRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getRoleName, roleVo.getRoleName()));
        if (!Objects.isNull(checkRole) && !roleVo.getRoleName().equals(checkRole.getRoleName())) {
            return Result.fail("角色名称不能重复");
        }
        Role updateRole = new Role();
        BeanUtils.copyProperties(roleVo, updateRole);
        updateRole.setUpdateBy(SecurityUtils.getUserName());
        updateRole.setUpdateTime(LocalDateTime.now());
        roleMapper.updateById(updateRole);

        // 修改角色权限部分
        roleMenuMapper.deleteByRole(roleVo.getRoleId());
        List<Long> menuList = roleVo.getMenuList().stream().map(Menu::getMenuId).collect(Collectors.toList());
        roleMenuMapper.addRoleMenu(roleVo.getRoleId(), menuList);
        return Result.success();
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @Override
    public Result delRole(Long roleId) {
        List<User> userList = userRoleMapper.selectUserByRoleId(roleId);
        if (CollUtil.isNotEmpty(userList)) {
            return Result.fail("删除失败，角色正在被使用");
        }
        // 删除角色表 逻辑删除
        Role delRole = new Role();
        delRole.setRoleId(roleId);
        delRole.setDelFlag(DelStausEnum.DEL.getCode());
        delRole.setUpdateBy(SecurityUtils.getUserName());
        delRole.setUpdateTime(LocalDateTime.now());
        roleMapper.updateById(delRole);
        // 删除角色用户关系表的数据
        userRoleMapper.deleteByRole(roleId);
        // 删除角色权限关系的数据
        roleMenuMapper.deleteByRole(roleId);
        return Result.success();
    }

    /**
     * 查询拥有该角色的所有用户
     * @param roleId
     * @return
     */
    @Override
    public List<User> getUserByRoleId(Long roleId) {
        return userRoleMapper.selectUserByRoleId(roleId);
    }
}
