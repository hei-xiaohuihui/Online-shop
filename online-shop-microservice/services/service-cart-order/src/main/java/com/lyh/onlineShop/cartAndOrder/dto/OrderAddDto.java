package com.lyh.onlineShop.cartAndOrder.dto;

import com.lyh.onlineShop.common.enumeration.OrderStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lyh
 *  创建订单数据传输对象
 */
@Data
public class OrderAddDto {

    @NotNull(message = "地址簿ID不能为空")
    private Integer addressBookId;

    // 订单状态：默认为待支付
    private Integer status = OrderStatusEnum.ORDER_STATUS_UNPAID.getCode();

    // 运费：默认为0
    private BigDecimal postage = BigDecimal.ZERO;

    // 支付方式
    private Integer payMethod;
}
