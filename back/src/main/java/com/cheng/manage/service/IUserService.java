package com.cheng.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.manage.common.model.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.model.User;
import com.cheng.manage.vo.TableList;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
public interface IUserService extends IService<User> {

    /**
     * 分页查询列表
     * @param queryUser
     * @return
     */
    TableList getUserList(PageParam<User> queryUser);


    /**
     * 新增用户
     * @param user
     */
    Result addUser(User user);

    /**
     * 通过用户id查询用户的角色列表
     * @param userId
     */
    Map<String, Object> getUserRoleList(Long userId);

    /**
     * 修改用户角色
     * @param userId
     * @param roleIds
     * @return
     */
    Result setUserRole(Long userId, List<Long> roleIds);

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Result updateUserPwd(String oldPassword, String newPassword);

    /**
     * 修改用户信息
     * @param sysUser
     * @return
     */
    Result updateUser(User sysUser);

    /**
     * 删除用户
     * @param userIds
     * @return
     */
    Result deleteUser(Long[] userIds);

    /**
     * 重置密码
     * @param user
     * @return
     */
    void resetPwd(User user);
}
