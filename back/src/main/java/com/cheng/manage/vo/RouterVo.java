package com.cheng.manage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name RouterVo
 * @date 2021/12/14 00:30
 */
@Data
public class RouterVo {
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

    @ApiModelProperty(value = "路由name")
    private String name;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "菜单状态（是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现）")
    private Boolean hidden;

    @ApiModelProperty(value = "重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击")
    private String redirect;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面")
    private Boolean alwaysShow;

    @ApiModelProperty(value = "子菜单")
    private List<RouterVo> children;
}
