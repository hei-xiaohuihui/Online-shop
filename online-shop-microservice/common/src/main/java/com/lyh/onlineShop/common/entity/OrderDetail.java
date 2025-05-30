package com.lyh.onlineShop.common.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lyh
 *  订单详情实体类
 */
@Data
@Builder
public class OrderDetail {

    private Integer id;

    private String orderNum;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
