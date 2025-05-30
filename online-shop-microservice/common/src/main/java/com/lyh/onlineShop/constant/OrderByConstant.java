package com.lyh.onlineShop.constant;

import java.util.Set;

/**
 * @author lyh
 *  排序规则常量类
 */
public class OrderByConstant {

    public static final String ORDER_BY_CREATE_TIME_ASC = "create_time asc";
    public static final String ORDER_BY_CREATE_TIME_DESC = "create_time desc";

    public static final String ORDER_BY_PRICE_ASC = "price asc";
    public static final String ORDER_BY_PRICE_DESC = "price desc";

    // 定义集合记录所有支持的排序规则
    public static final Set<String> ORDER_BY_SET = Set.of(
            ORDER_BY_CREATE_TIME_ASC,
            ORDER_BY_CREATE_TIME_DESC,
            ORDER_BY_PRICE_ASC,
            ORDER_BY_PRICE_DESC);
}
