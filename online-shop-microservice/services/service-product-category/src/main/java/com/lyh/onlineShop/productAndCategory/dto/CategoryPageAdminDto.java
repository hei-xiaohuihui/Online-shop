package com.lyh.onlineShop.productAndCategory.dto;

import lombok.Data;

/**
 * @author lyh
 *  商品分类分页查询数据传输对象——管理端
 */
@Data
public class CategoryPageAdminDto {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    // 分类名称，用于模糊查询
    private String name;

    // 分类级别，用于条件查询
    private Integer type;
}
