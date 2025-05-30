package com.lyh.onlineShop.cartAndOrder.service;

import com.github.pagehelper.PageInfo;
import com.lyh.onlineShop.cartAndOrder.dto.OrderAddDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderCancelDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderPageAdminDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderPageUserDto;
import com.lyh.onlineShop.cartAndOrder.vo.OrderVo;

public interface OrderService {

    /**
     *  管理端
     */
    PageInfo pageQueryForAdmin(OrderPageAdminDto orderPageAdminDto);

    void deliveryOrder(String orderNum);


    /**
     *  用户端
     */
    String addOrder(OrderAddDto orderAddDto, Integer userId);

    OrderVo getOrderDetailForUser(String orderNum, Integer userId);

    void cancelOrder(OrderCancelDto orderCancelDto, Integer userId);

    String getQrCode(String orderNum);

    void payOrder(String orderNum, Integer userId);

    void completeOrder(String orderNum, Integer userId);

    PageInfo pageQueryForUser(OrderPageUserDto orderPageUserDto, Integer userId);
}
