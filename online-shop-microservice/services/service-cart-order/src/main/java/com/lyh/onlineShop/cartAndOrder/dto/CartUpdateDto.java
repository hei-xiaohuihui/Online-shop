package com.lyh.onlineShop.cartAndOrder.dto;

import lombok.Data;

/**
 * @author lyh
 *  更新购物车数据传输对象
 */
@Data
public class CartUpdateDto {

    private Integer productId;

    private Integer quantity;

    private Integer selected;
}
