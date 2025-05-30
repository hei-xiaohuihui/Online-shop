package com.lyh.onlineShop.common.exception;

import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import lombok.Data;

/**
 * @author lyh
 *  自定义业务异常类
 */
@Data
public class BaseException extends RuntimeException{

    private Integer code;

    private String msg;

    public BaseException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseException(ExceptionEnum ex) {
        this(ex.getCode(), ex.getMsg());
    }

}
