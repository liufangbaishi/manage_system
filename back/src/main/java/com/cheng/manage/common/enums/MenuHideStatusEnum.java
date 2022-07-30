package com.cheng.manage.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 菜单是否隐藏
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name MenuStautsEnum
 * @date 2022/7/30 19:08
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MenuHideStatusEnum {
    HIDE("1", "隐藏"),
    DISPLAY("0", "显示");

    private String code;

    private String value;

}