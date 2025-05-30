package com.lyh.onlineShop.cartAndOrder.dto;

import lombok.Data;

/**
 * @author lyh
 *  用户取消订单数据传输对象
 */
@Data
public class OrderCancelDto {

    private String orderNum;

    private String cancelReason;
}
