package com.cheng.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "输入的验证码")
    private String code;

    @ApiModelProperty(value = "验证码对应的key")
    private String uuid;
}
