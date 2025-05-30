package com.lyh.onlineShop.cartAndOrder.cotroller.admin;

import com.lyh.onlineShop.cartAndOrder.dto.OrderPageAdminDto;
import com.lyh.onlineShop.cartAndOrder.service.OrderService;
import com.lyh.onlineShop.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyh
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService  orderService;

    /**
     *  订单分页查询
     * @param orderPageAdminDto
     * @return
     */
    @GetMapping("/page")
    public Result pageQuery(OrderPageAdminDto orderPageAdminDto) {
        return Result.success(orderService.pageQueryForAdmin(orderPageAdminDto));
    }

    /**
     *  管理员发货
     * @param orderNum
     * @return
     */
    @PostMapping("/delivery")
    public Result deliveryOrder(@RequestParam String orderNum) {
        orderService.deliveryOrder(orderNum);
        return Result.success();
    }
}
