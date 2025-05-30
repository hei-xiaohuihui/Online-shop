package com.lyh.onlineShop.cartAndOrder.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lyh
 *  用户购物车视图对象
 */
@Data
public class CartVo {

    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer selected;

    // 商品单价
    private BigDecimal price;

    // 商品名称
    private String productName;

    // 商品图片
    private String productImage;

    // 对应商品的总价（需要额外计算）
    private BigDecimal totalPrice;
}
