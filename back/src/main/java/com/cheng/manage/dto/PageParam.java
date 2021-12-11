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
public class PageParam<T>  implements IPage {
    //  description = "页码", defaultValue =  1
    @ApiModelProperty(value = "页码")
    private Integer pageNum = 1;

    //	description = "页数", defaultValue = 20
    @ApiModelProperty(value = "页数")
    private Integer pageSize = 20;

    //	description = "排序", example = "id desc"
    @ApiModelProperty(value = "排序")
    private String orderBy;

    //  description = "参数"
    @ApiModelProperty(value = "参数")
    private T param;

}
