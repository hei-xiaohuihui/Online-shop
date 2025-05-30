package com.lyh.onlineShop.common.enumeration;

/**
 *  异常枚举
 */
public enum ExceptionEnum {
    /**
     *  用户模块相关异常：11000-11999
     */
    USER_NEED_ADMIN(11001, "非管理员用户"),
    USER_NEED_LOGIN(11002, "用户未登录"),
    USER_EXIST(11003, "用户已存在"),
    USER_NAME_OR_PASSWORD_ERROR(11004, "用户名或密码错误"),

    ADDRESS_BOOK_NOT_ADD(11101, "用户未添加地址信息"),
    ADDRESS_BOOK_NOT_EXIST(11102, "地址信息不存在"),

    /**
     *  商品模块相关异常：12000-12999
     */
    PRODUCT_EXIST(12001, "商品已存在"),
    PRODUCT_NOT_EXIST(12002, "商品不存在"),
    PRODUCT_NOT_AVAILABLE(12003, "商品已下架"),
    PRODUCT_NOT_ENOUGH(12004, "商品库存不足"),

    CATEGORY_EXIST(12101, "商品分类已存在"),
    CATEGORY_NOT_EXIST(12101, "商品分类不存在"),

    IMAGE_UPLOAD_EMPTY(12201, "上传的图片文件为空"),
    IMAGE_UPLOAD_FAILED(12202, "图片上传失败"),

    /**
     *  购物车模块相关异常：13000-13999
     */
    CART_NOT_EXIST(13001, "购物车信息不存在"),
    CART_IS_EMPTY(13002, "购物车为空"),
    CART_NOT_PRODUCT_SELECTED(13003, "购物车未勾选商品"),

    /**
     *  订单模块相关异常：14000-14999
     */
    ORDER_NOT_EXIST(14001, "订单不存在"),
    ORDER_STATUS_ERROR(14002, "订单状态错误"),
    ORDER_NOT_BELONG_TO_USER(14003, "非当前用户订单"),

    /**
     *  数据库操作相关异常：20000-20999
     */
    DB_INSERT_FAILED(20001, "数据库插入数据失败"),
    DB_UPDATE_FAILED(20002, "数据库更新数据失败"),
    DB_DELETE_FAILED(20003, "数据库删除数据失败"),


    /**
     *  远程调用相关异常：30000-30999
     */
    REMOTE_CALL_ERROR(30001, "远程调用失败"),

    /**
     *  其他异常：40000-40999
     */
    ENUM_ORDER_STATUS_NOT_EXIST(40001, "订单状态枚举不存在"),
    ENUM_PAY_METHOD_NOT_EXIST(40002, "支付方式枚举不存在"),

    /**
     *  系统错误/异常
     */
    SYSTEM_ERROR(50001, "系统错误"),
    SYSTEM_FILE_CREATE_FAILED(50002, "系统创建文件失败");

    private Integer code;
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
