package com.cheng.manage.service.impl;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.manage.common.Result;
import com.cheng.manage.dto.PageParam;
import com.cheng.manage.mapper.UserMapper;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IUserService;
import com.cheng.manage.vo.TableList;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public TableList getUserList(PageParam<User> queryUser) {
        PageInfo<User> pageUserList = PageHelper
                .startPage(queryUser.getPageNum(), queryUser.getPageSize())
                .doSelectPageInfo(() -> getAllUserList(queryUser.getParam()));

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
            return Result.fail("用户名已存在");
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
}
