package com.lyh.onlineShop.cartAndOrder.mapper;

import com.lyh.onlineShop.cartAndOrder.dto.OrderPageAdminDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderPageUserDto;
import com.lyh.onlineShop.common.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     *  添加订单
     * @param order
     * @return
     */
    int addOrder(Order order);

    /**
     *  根据订单号查询订单
     * @param orderNum
     * @return
     */
    Order selectByOrderNum(String orderNum);

    /**
     *  更新订单状态
     * @param order
     * @return
     */
//    @TimeAutoFill
    int updateOrderStatus(Order order);

    /**
     *  根据条件查询订单信息——管理端
     * @param orderPageAdminDto
     * @return
     */
    List<Order> selectByCondition(OrderPageAdminDto orderPageAdminDto);

    /**
     *  根据条件查询订单信息——用户端
     * @param orderPageUserDto
     * @param userId
     * @return
     */
    List<Order> selectByConditionForUser(@Param("dto") OrderPageUserDto orderPageUserDto, @Param("userId") Integer userId);

}
