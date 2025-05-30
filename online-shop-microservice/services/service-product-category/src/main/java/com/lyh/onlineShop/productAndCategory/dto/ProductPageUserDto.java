package com.lyh.onlineShop.productAndCategory.dto;

import lombok.Data;

/**
 * @author lyh
 *  商品分页查询数据传输对象——用户端
 */
@Data
public class ProductPageUserDto {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    // 商品名称，用于模糊查询
    private String name;

    // 商品类别，用于条件查询
    private Integer categoryId;

    // 排序字段，用于排序
    private String orderBy;
}
