package com.cheng.manage.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name TableResult
 * @date 2021/11/28 00:06
 */
@Data
@Builder
public class TableList {

    private long total;

    private List<?> list;
}
