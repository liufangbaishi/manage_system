package com.cheng.manage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheng.manage.common.consts.AuthConsts;
import com.cheng.manage.common.enums.MenuHideStatusEnum;
import com.cheng.manage.common.model.Result;
import com.cheng.manage.common.consts.ResultCode;
import com.cheng.manage.common.exception.LoginException;
import com.cheng.manage.dto.LoginParam;
import com.cheng.manage.mapper.MenuMapper;
import com.cheng.manage.mapper.UserMapper;
import com.cheng.manage.mapper.UserRoleMapper;
import com.cheng.manage.model.LoginUser;
import com.cheng.manage.model.Menu;
import com.cheng.manage.model.Role;
import com.cheng.manage.model.User;
import com.cheng.manage.service.IAuthService;
import com.cheng.manage.utils.BuildTree;
import com.cheng.manage.utils.IpUtils;
import com.cheng.manage.utils.JwtUtils;
import com.cheng.manage.utils.SecurityUtils;
import com.cheng.manage.vo.RouterVo;
import com.cheng.manage.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedList;
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
    private MenuMapper menuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录
     */
    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录
     * @param loginParam
     * @param request
     * @return
     */
    @Override
    public String login(LoginParam loginParam, HttpServletRequest request) {
        // 判断验证码
        validate(loginParam);
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
        // 获取登录信息
        LoginUser userInfo = (LoginUser) authentication.getPrincipal();

        // 更新最后登录时间,登录地址
        updateLoginInfo(userInfo, request);

        // 生成token
        return jwtUtils.generateToken(userInfo);
    }

    /**
     * 更新登录信息
     * @param userInfo
     * @param request
     */
    private void updateLoginInfo(LoginUser userInfo, HttpServletRequest request) {
        User updateUser = new User();
        updateUser.setUserId(userInfo.getUserId());
        updateUser.setLoginIp(IpUtils.getIpAddr(request));
        updateUser.setLoginDate(LocalDateTime.now());
        userMapper.updateById(updateUser);
    }

    /**
     * 校验验证码
     * @param loginParam
     */
    private void validate(LoginParam loginParam) {
        String code = loginParam.getCode();
        String uuid = loginParam.getUuid();
        if (StrUtil.isEmpty(code) || StrUtil.isEmpty(uuid)) {
            throw new LoginException("验证码不能为空");
        }
        Object redisCode = redisTemplate.opsForValue().get(AuthConsts.CAPTCHA_KEY + uuid);
        if (!code.equals(redisCode)) {
            throw new LoginException("验证码错误");
        }
        redisTemplate.delete(AuthConsts.CAPTCHA_KEY+uuid);
    }


    /**
     * 退出登录
     * @param token
     * @return
     */
    @Override
    public String logout(String token) {
        redisTemplate.delete(AuthConsts.AUTHORITY +  SecurityUtils.getUserId());
        SecurityUtils.setCurrentUser(null);
        return "退出登录成功!";
    }


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
//        List<Role> roles = userRoleMapper.selectUserRoleList(userId);
//        if (CollUtil.isNotEmpty(roles)) {
//            String roleAuthorites = roles.stream().map(e -> AuthConsts.ROLE_PREFIX + e.getRoleKey()).collect(Collectors.joining(","));
//            authorites = roleAuthorites.concat(",");
//        }
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

    /**
     * 查询菜单
     * @return
     */
    @Override
    public Result getCurrentNav() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            return Result.fail(ResultCode.TOKEN_EXPIRE, "用户信息不存在，重新登录");
        }
        // 查询菜单
        List<Menu> menuList = menuMapper.selectUserNavList(currentUser.getUserId());
        List<Menu> menuTree = BuildTree.buildTree(menuList);
        List<RouterVo> routerVoList = buildRouter(menuTree);
        // 查询按钮
        String authority = getAuthority(currentUser.getUserId());
        String[] authorities = StringUtils.tokenizeToStringArray(authority, ",");

        return Result.success(MapUtil.builder()
                .put("authorities", authorities)
                .put("nav", routerVoList)
                .map());
    }

    private List<RouterVo> buildRouter(List<Menu> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (Menu menu : menus) {
            RouterVo routerVo = new RouterVo();
            routerVo.setMenuId(menu.getMenuId());
            routerVo.setHidden(MenuHideStatusEnum.HIDE.getCode().equals(menu.getVisible())); // 1 为隐藏 hidden为true
            // 首字母大写
            String routerName = menu.getPath().substring(0, 1).toUpperCase() + menu.getPath().substring(1);
            routerVo.setName(routerName);
            routerVo.setPath(getPath(menu));
            routerVo.setComponent(getComponent(menu));
            routerVo.setMenuName(menu.getMenuName());
            routerVo.setIcon(menu.getIcon());
            List<Menu> menuChildren = menu.getChildren();
            if (CollUtil.isNotEmpty(menuChildren) && "M".equals(menu.getMenuType())) {
                routerVo.setRedirect("noRedirect");
                routerVo.setAlwaysShow(true);
                routerVo.setChildren(buildRouter(menuChildren));
            }
            routers.add(routerVo);
        }
        return routers;
    }
    /**
     * 获取组件信息
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(Menu menu) {
        String component = menu.getComponent();
        // M目录 C菜单
        if (StrUtil.isEmpty(component) && "M".equals(menu.getMenuType())) {
            // componet 前端对应组件 一级目录Layout 二级目录ParentView
            component = menu.getParentId() == 0 ? "Layout" : "ParentView";
        }
        return component;
    }
    /**
     * 获取路径信息
     * @param menu 菜单信息
     * @return 路径信息
     */
    public String getPath(Menu menu) {
        String routerPath = menu.getPath();
        // M目录 C菜单
        if (0 == menu.getParentId().intValue() && "M".equals(menu.getMenuType())) {
            routerPath = "/" + menu.getPath();
        }
        return routerPath;
    }

    @Override
    public Result getCurrentUser() {
        User currentUser = SecurityUtils.getCurrentUser();
        currentUser.setPassword(null);
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(currentUser, userVo);
        List<Role> roles = userRoleMapper.selectUserRoleList(currentUser.getUserId());
        if (CollUtil.isNotEmpty(roles)) {
            userVo.setRoles(roles.stream().map(Role::getRoleKey).collect(Collectors.toList()));
            userVo.setRolesName(roles.stream().map(Role::getRoleName).collect(Collectors.toList()));
        }
        return Result.success(userVo);
    }
}
