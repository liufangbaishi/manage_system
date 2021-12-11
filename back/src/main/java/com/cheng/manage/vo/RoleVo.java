package com.cheng.manage.vo;

import com.cheng.manage.model.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name RoleVo
 * @date 2021/11/28 19:36
 */
@Data
public class RoleVo {
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色权限字符串")
    private String roleKey;

    @ApiModelProperty(value = "显示顺序")
    private Integer roleSort;

    @ApiModelProperty(value = "角色状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value = "角色对应的菜单")
    private List<Menu> menuList;
}
