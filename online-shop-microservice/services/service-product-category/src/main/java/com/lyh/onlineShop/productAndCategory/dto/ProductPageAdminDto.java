package com.lyh.onlineShop.productAndCategory.dto;

import lombok.Data;

/**
 * @author lyh
 *  商品分页查询数据传输对象——管理端
 */
@Data
public class ProductPageAdminDto {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    // 商品名称，用于模糊查询
    private String name;

    // 商品状态，用于条件查询
    private Integer status;
}
