package com.lyh.onlineShop.cartAndOrder.mapper;

import com.lyh.onlineShop.cartAndOrder.vo.OrderDetailVo;
import com.lyh.onlineShop.common.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     *  批量添加订单详情（将订单详情加入订单表中）
     * @param orderDetailList
     * @return
     */
    int batchAddOrderDetail(@Param("orderDetailList") List<OrderDetail> orderDetailList);

    /**
     *  根据订单号查询订单详情
     * @param orderNum
     * @return
     */
    List<OrderDetailVo> selectByOrderNum(String orderNum);
}
