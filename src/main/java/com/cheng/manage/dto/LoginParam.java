package com.cheng.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name LoginParam
 * @date 2021/12/4 23:22
 */
@Data
@ApiModel(value="登录对象", description="登录")
public class LoginParam {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    private String code;

    private String uuid;
}
