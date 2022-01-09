package com.cheng.manage.vo;

import com.cheng.manage.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name UserVo
 * @date 2021/12/13 22:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVo extends User {

    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "最后登录IP")
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "角色")
    List<String> roles;

    @ApiModelProperty(value = "角色名称")
    List<String> rolesName;
}
