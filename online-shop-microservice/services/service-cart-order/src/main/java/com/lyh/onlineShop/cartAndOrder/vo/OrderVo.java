package com.lyh.onlineShop.cartAndOrder.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lyh
 *  订单信息视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {

//    private Integer id;
    private String orderNum;

    private Integer userId;

    private BigDecimal totalPrice;

    private String consignee;

    private String phone;

    private String address;

    private Integer status;

    private String orderStatusStr;

    private BigDecimal postage;

//    private Integer payMethod;
    private String payMethodStr;

    private LocalDateTime payTime;

    private String cancelReason;

    private LocalDateTime cancelTime;

    private LocalDateTime deliveryTime;

    private LocalDateTime completeTime;

    private LocalDateTime createTime;

    /**
     *  订单对应的订单详情列表（从订单详情表中查出）
     */
    private List<OrderDetailVo> orderDetailVoList;
}
