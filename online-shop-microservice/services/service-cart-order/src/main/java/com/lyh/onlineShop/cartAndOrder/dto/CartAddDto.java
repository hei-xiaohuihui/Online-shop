package com.lyh.onlineShop.cartAndOrder.dto;

import lombok.Data;

/**
 * @author lyh
 *  购物车添加商品数据传输对象
 */
@Data
public class CartAddDto {

    // 要添加的商品id
    private Integer productId;

    // 要添加的商品数量
    private Integer count;
}
