package com.cheng.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.manage.common.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.model.User;
import com.cheng.manage.vo.TableList;

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
}
