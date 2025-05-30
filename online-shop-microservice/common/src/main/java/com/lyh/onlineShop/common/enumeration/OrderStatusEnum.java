package com.lyh.onlineShop.common.enumeration;

import com.lyh.onlineShop.common.exception.BaseException;

/**
 *  订单状态枚举
 */
public enum OrderStatusEnum {

    ORDER_STATUS_CANCELLED(0, "已取消"),
    ORDER_STATUS_UNPAID(10, "待付款"),
    ORDER_STATUS_PAID(20, "已付款"),
    ORDER_STATUS_DELIVERED(30, "已发货"),
    ORDER_STATUS_COMPLETED(40, "已完成");

    private Integer code;
    private String value;

    OrderStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }

    public static OrderStatusEnum codeOf(Integer code) {
        for(OrderStatusEnum orderstatusEnum : values()) {
            if(orderstatusEnum.code.equals(code)) {
                return orderstatusEnum;
            }
        }
        throw new BaseException(ExceptionEnum.ENUM_ORDER_STATUS_NOT_EXIST);
    }


}
