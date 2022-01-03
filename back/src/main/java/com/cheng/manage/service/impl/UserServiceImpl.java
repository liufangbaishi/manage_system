package com.cheng.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.manage.common.consts.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.mapper.UserMapper;
import com.cheng.manage.mapper.UserRoleMapper;
import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IRoleService;
import com.cheng.manage.service.IUserService;
import com.cheng.manage.utils.SecurityUtils;
import com.cheng.manage.vo.TableList;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public TableList getUserList(PageParam<User> queryUser) {
        PageInfo<User> pageUserList = PageHelper
                .startPage(queryUser.getPageNum(), queryUser.getPageSize())
                .doSelectPageInfo(() -> getAllUserList(queryUser.getParams()));

        return TableList.builder().total(pageUserList.getTotal()).list(pageUserList.getList()).build();
    }

    /**
     * 查询全部用户
     * @param user
     */
    private void getAllUserList(User user) {
        userMapper.getUserList(user);
    }

    @Override
    public Result addUser(User user) {
        User checkUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, user.getUserName()));
        if (!Objects.isNull(checkUser)) {
            return Result.fail("新增用户失败，用户名已存在");
        }
        // 判断电话号是否唯一
        if (StrUtil.isNotEmpty(user.getPhoneNumber())) {
            List<User> userList = userMapper.checkPhoneNumber(user);
            if (CollUtil.isNotEmpty(userList) && userList.size() > 1) {
                return Result.fail("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
            }
        }
        // 生成一个随机密码
        String password = "123456";
        Digester digester = new Digester(DigestAlgorithm.MD5);
        user.setPassword(digester.digestHex(password));
        user.setCreateBy("admin");
        user.setUpdateBy("admin");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
        return Result.success("新增用户成功");
    }

    @Override
    public Map<String, Object> getUserRoleList(Long userId) {
        List<Role> allRoleList = roleService.getAllRoleList(new Role());
        List<Long> userRoleIds = userRoleMapper.selectUserRoleList(userId).stream().map(Role::getRoleId).collect(Collectors.toList());
        allRoleList.forEach(item -> item.setFlag(userRoleIds.contains(item.getRoleId())));

        Map<String, Object> result = new HashMap<>();
        result.put("roles", allRoleList);
        result.put("user", userMapper.selectById(userId));
        return result;
    }

    @Override
    public Result updateUserPwd(String oldPassword, String newPassword) {
        User loginUser = SecurityUtils.getCurrentUser();
        User user = userMapper.selectById(loginUser.getUserId());
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.fail("修改失败，原密码错误");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return Result.fail("新密码不能与旧密码相同");
        }
        User updateUser = new User();
        updateUser.setUserId(loginUser.getUserId());
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        if (updateUserInfo(updateUser) > 0) {
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    public Result updateUser(User sysUser) {
        // 判断电话号是否唯一
        if (StrUtil.isNotEmpty(sysUser.getPhoneNumber())) {
            List<User> userList = userMapper.checkPhoneNumber(sysUser);
            if (CollUtil.isNotEmpty(userList) && userList.size() > 1) {
                return Result.fail("修改用户'" + sysUser.getUserName() + "'失败，手机号码已存在");
            }
        }
        sysUser.setUpdateBy(SecurityUtils.getUserName());
        if (updateUserInfo(sysUser) > 0) {
            return Result.success();
        }
        return Result.fail();
    }


    @Override
    public Result deleteUser(Long[] userIds) {
        // 删除用户角色关系
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户 逻辑删除
        if (userMapper.deleteUserByIds(userIds) > 0) {
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    public void resetPwd(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUserName());
        updateUserInfo(user);
    }

    public int updateUserInfo(User sysUser) {
        return userMapper.updateUser(sysUser);
    }
}
