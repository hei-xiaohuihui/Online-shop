package com.lyh.onlineShop.productAndCategory.query;

import lombok.Data;

import java.util.List;

/**
 * @author lyh
 *  用户端商品列表查询
 */
@Data
public class ProductListQuery {

    // 商品名称，用户模糊查询
    private String name;

    // 记录需要查询的商品分类id（categoryId）的列表——根据categoryId查询该类别下的所有商品
    private List<Integer> categoryIds;
}
