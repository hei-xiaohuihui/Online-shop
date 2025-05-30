package com.lyh.onlineShop.cartAndOrder.dto;

import lombok.Data;

/**
 * @author lyh
 */
@Data
public class OrderPageAdminDto {

    private int pageNum = 1;

    private int pageSize = 10;

    /**
     *  支持模糊查询和条件查询
     */
    // 订单号
    private String orderNum;

    // 收货人
    private String consignee;

    // 手机号
    private String phone;

    // 收货地址
    private String address;

    // 订单状态
    private Integer status;

    // 支付方式
    private Integer payMethod;
}
