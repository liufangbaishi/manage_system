package com.cheng.manage.mapper;

import com.cheng.manage.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> getUserList(User userParam);
}
