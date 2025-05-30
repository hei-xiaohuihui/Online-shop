package com.lyh.onlineShop.common.utils;

import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import lombok.Data;

/**
 * @author lyh
 *  统一结果返回封装类
 */
@Data // 不提供get/set方法的话，Jackson就无法将Result序列化
public class Result <T>{
    // 响应码
    private Integer code;
    // 提示信息
    private String msg;
    // 返回的数据
    private T data;

    // 默认的成功响应码的提示信息
    public static final Integer SUCCESS_CODE = 200;
    public static final String SUCCESS_MSG = "success";

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this(code, msg, null);
    }

    public Result() {
        this(SUCCESS_CODE, SUCCESS_MSG);
    }

    /**
     *  成功返回情况
     */
    // 默认成功
    public static <T> Result<T> success() {
        return new Result<>();
    }

    // 成功，传入数据
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, SUCCESS_MSG, data);
    }


    /**
     *  错误返回情况
     */
    // 错误，传入异常枚举类型
    public static <T> Result<T> error(ExceptionEnum ex) {
        return new Result<>(ex.getCode(), ex.getMsg());
    }

    // 错误，传入错误码和错误信息
    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg);
    }
}
