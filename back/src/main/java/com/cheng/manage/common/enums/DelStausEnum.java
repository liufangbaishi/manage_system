package com.cheng.manage.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name DelStausEnum
 * @date 2022/5/29 15:44
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DelStausEnum {
    DEL("2", "删除"),
    DISABLED("1", "禁用"),
    NORMAL("0", "正常");

    private String code;

    private String value;

}
