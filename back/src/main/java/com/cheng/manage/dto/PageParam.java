package com.cheng.manage.dto;

import com.github.pagehelper.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name PageParam
 * @date 2021/11/21 22:13
 */
@Data
@Accessors(chain = true)
public class PageParam<T> implements IPage {
    @ApiModelProperty(value = "页码", allowableValues = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页数", allowableValues = "10")
    private Integer pageSize = 20;

    @ApiModelProperty(value = "排序", allowableValues = "id desc")
    private String orderBy;

    @ApiModelProperty(value = "参数")
    private T params;

}
