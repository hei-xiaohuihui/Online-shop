package com.lyh.onlineShop.common.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lyh
 *  订单实体类
 */
@Data
@Builder
public class Order {

    private Integer id;

    private String orderNum;

    private Integer userId;

    private BigDecimal totalPrice;

    private String consignee;

    private String phone;

    private String address;

    private Integer status;

    private BigDecimal postage;

    private Integer payMethod;

    private LocalDateTime payTime;

    private String cancelReason;

    private LocalDateTime cancelTime;

    private LocalDateTime deliveryTime;

    private LocalDateTime completeTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
