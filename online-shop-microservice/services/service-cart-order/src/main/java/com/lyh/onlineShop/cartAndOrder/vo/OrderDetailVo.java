package com.lyh.onlineShop.cartAndOrder.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lyh
 *  订单详情视图对象
 */
@Data
public class OrderDetailVo {

    private String orderNum;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;
}
