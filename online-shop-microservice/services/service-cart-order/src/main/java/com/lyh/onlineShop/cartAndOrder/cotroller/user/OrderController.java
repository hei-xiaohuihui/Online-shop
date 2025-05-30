package com.lyh.onlineShop.cartAndOrder.cotroller.user;

import com.lyh.onlineShop.cartAndOrder.dto.OrderAddDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderCancelDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderPageUserDto;
import com.lyh.onlineShop.cartAndOrder.service.OrderService;
import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.constant.JwtClaimsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyh
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     *  创建订单
     * @param orderAddDto
     * @return
     */
    @PostMapping("/add")
    public Result addOrder(@RequestBody OrderAddDto orderAddDto, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(orderService.addOrder(orderAddDto, Integer.valueOf(userIdStr)));
    }

    /**
     *  查询订单详情
     * @param orderNum
     * @return
     */
    @GetMapping("/detail")
    public Result getOrderDetail(@RequestParam String orderNum, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(orderService.getOrderDetailForUser(orderNum, Integer.valueOf(userIdStr)));
    }

    /**
     *  查询订单列表
     * @param orderPageUserDto
     * @param userIdStr
     * @return
     */
    @GetMapping("/page")
    public Result getOrderList(OrderPageUserDto orderPageUserDto, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(orderService.pageQueryForUser(orderPageUserDto, Integer.valueOf(userIdStr)));
    }

    /**
     *  用户取消订单
     * @param orderCancelDto
     * @return
     */
    @PostMapping("/cancel")
    public Result cancelOrder(@RequestBody OrderCancelDto orderCancelDto, @RequestHeader(JwtClaimsConstant.USER_ID) String  userIdStr) {
        orderService.cancelOrder(orderCancelDto, Integer.valueOf(userIdStr));
        return Result.success();
    }

    /**
     *  获取支付二维码
     * @param orderNum
     * @return
     */
    @GetMapping("/qrcode")
    public Result getQrCode(@RequestParam String orderNum) {
        return Result.success(orderService.getQrCode(orderNum));
    }

    /**
     *  用户支付订单
     * @param orderNum
     * @return
     */
    @PostMapping("/pay")
    public Result payOrder(@RequestParam String orderNum, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        orderService.payOrder(orderNum, Integer.valueOf(userIdStr));
        return Result.success();
    }

    /**
     *  用户完成订单
     * @param orderNum
     * @return
     */
    @PostMapping("/complete")
    public Result completeOrder(@RequestParam String orderNum, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        orderService.completeOrder(orderNum, Integer.valueOf(userIdStr));
        return Result.success();
    }
}
