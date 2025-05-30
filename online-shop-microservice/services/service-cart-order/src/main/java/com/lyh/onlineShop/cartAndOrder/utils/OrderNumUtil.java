package com.lyh.onlineShop.cartAndOrder.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lyh
 *  生成订单编号工具类：利用hutool工具使用雪花算法生成全局唯一的订单编号
 */
@Component
public class OrderNumUtil {

//    @Value("${lyh.orderNum.workerId") // 无法注入
//    private static long workerId;
//
//    @Value("${lyh.orderNum.dataCenterId")
//    private static long dataCenterId;

//    private static final Snowflake snowflake = new Snowflake(workerId, dataCenterId);

    /**
     *  初始化雪花算法实例：workerId表示机器编码（机器编号），dataCenterId表示数据中心编号，二者用于在分布式系统中确保唯一性
     */
    private final Snowflake snowflake;

    @Autowired
    public OrderNumUtil(@Value("${lyh.orderNum.workerId}") long workerId, @Value("${lyh.orderNum.dataCenterId}") long dataCenterId) {
        this.snowflake = new Snowflake(workerId, dataCenterId);
    }

    /**
     *  生成订单编号
     * @return
     */
    public String generateOrderNum() {
        // 获取下单时间
        String dataTimeStr = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");

        // 生成订单编号：下单时间 + 雪花算法生成的唯一id
        return dataTimeStr + snowflake.nextIdStr();
    }
}
