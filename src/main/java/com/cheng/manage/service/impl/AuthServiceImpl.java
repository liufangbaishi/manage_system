package com.cheng.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheng.manage.common.consts.AuthConsts;
import com.cheng.manage.common.exception.LoginException;
import com.cheng.manage.dto.LoginParam;
import com.cheng.manage.mapper.MenuMapper;
import com.cheng.manage.mapper.RoleMapper;
import com.cheng.manage.mapper.UserMapper;
import com.cheng.manage.model.CurrentUser;
import com.cheng.manage.model.Menu;
import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IAuthService;
import com.cheng.manage.utils.IpUtils;
import com.cheng.manage.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name AuthServiceImpl
 * @date 2021/12/1 23:53
 */
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录
     */
    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User getUserByUserName(String userName) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserName, userName)
                .eq(User::getDelFlag, 0));
    }

    @Override
    public String getAuthority(Long userId) {
        // 先在redis中查询
        String userAuthority = (String)redisTemplate.opsForValue().get(AuthConsts.AUTHORITY + userId);
        if (!StrUtil.isBlankOrUndefined(userAuthority)) {
            return userAuthority;
        }
        String authorites = "";
        // 查询用户角色
        List<Role> roles = roleMapper.selectUserRoleList(userId);
        if (CollUtil.isNotEmpty(roles) && roles.size() > 0) {
            String roleAuthorites = roles.stream().map(e -> AuthConsts.ROLE_PREFIX + e.getRoleKey()).collect(Collectors.joining(","));
            authorites = roleAuthorites.concat(",");
        }
        // 查询用户权限
        List<Menu> menus = menuMapper.selectUserMenuList(userId);
        if (CollUtil.isNotEmpty(menus) && menus.size() > 0) {
            String menuAuthorites = menus.stream().map(Menu::getPerms).collect(Collectors.joining(","));
            authorites = authorites.concat(menuAuthorites);
        }
        // 缓存到redis中，设置过期时间为60*60秒
        redisTemplate.opsForValue().set(AuthConsts.AUTHORITY+userId, authorites, 60 * 60, TimeUnit.SECONDS);
        return authorites;
    }

    @Override
    public String login(LoginParam loginParam, HttpServletRequest request) {
        // 判断验证码
        // 执行登录方法
        Authentication authentication = null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginParam.getUserName(), loginParam.getPassword()));
        } catch (BadCredentialsException e) {
            log.info("用户名或密码错误");
            throw new LoginException("用户名或密码错误");
        } catch (Exception e) {
            log.info("登录异常", e);
            throw new LoginException(e.getMessage());
        }
        CurrentUser userInfo = (CurrentUser) authentication.getPrincipal();
        // 更新最后登录时间,登录地址
        User updateUser = new User();
        updateUser.setUserId(userInfo.getUserId());
        updateUser.setLoginIp(IpUtils.getIpAddr(request));
        updateUser.setLoginDate(LocalDateTime.now());
        userMapper.updateById(updateUser);
        // 生成token
        return jwtUtils.generateToken(userInfo);
    }
}
