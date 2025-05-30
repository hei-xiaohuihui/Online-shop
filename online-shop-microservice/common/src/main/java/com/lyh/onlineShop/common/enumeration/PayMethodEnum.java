package com.lyh.onlineShop.common.enumeration;

import com.lyh.onlineShop.common.exception.BaseException;

/**
 *  支付方式枚举
 */
public enum PayMethodEnum {

    PAY_METHOD_ALIPAY(1, "支付宝"),
    PAY_METHOD_WECHAT(2, "微信");

    private Integer code;
    private String value;

    PayMethodEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static PayMethodEnum codeOf(Integer code) {
        for(PayMethodEnum payMethodEnum : values()) {
            if(payMethodEnum.code.equals(code)) {
                return payMethodEnum;
            }
        }
        throw new BaseException(ExceptionEnum.ENUM_PAY_METHOD_NOT_EXIST);
    }
}
