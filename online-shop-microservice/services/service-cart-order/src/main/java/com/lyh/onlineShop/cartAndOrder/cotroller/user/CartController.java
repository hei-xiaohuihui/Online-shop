package com.lyh.onlineShop.cartAndOrder.cotroller.user;

import com.lyh.onlineShop.cartAndOrder.dto.CartAddDto;
import com.lyh.onlineShop.cartAndOrder.dto.CartUpdateDto;
import com.lyh.onlineShop.cartAndOrder.service.CartService;
import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.constant.JwtClaimsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyh
 */
@RestController
@RequestMapping("/user/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     *  获取购物车列表
     * @param userIdStr
     * @return
     */
    @GetMapping("/list")
    public Result getCart(@RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(cartService.getCart(Integer.valueOf(userIdStr)));
    }

    /**
     *  确认订单界面获取用户购物车中勾选的商品信息
     * @param userIdStr
     * @return
     */
    @GetMapping("/check")
    public Result checkCart(@RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(cartService.checkCart(Integer.valueOf(userIdStr)));
    }

    /**
     *  添加商品到购物车
     * @param cartAddDto
     * @return
     */
    @PostMapping("/add")
    public Result addCart(@RequestBody CartAddDto cartAddDto, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(cartService.addCart(cartAddDto,  Integer.valueOf(userIdStr)));
    }

    /**
     *  更新购物车
     * @param cartUpdateDto
     * @return
     */
    @PostMapping("/update")
    public Result updateCart(@RequestBody CartUpdateDto cartUpdateDto, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(cartService.updateCart(cartUpdateDto, Integer.valueOf(userIdStr)));
    }

    /**
     *  删除购物车商品
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteCart(@RequestParam("productId") Integer productId, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(cartService.deleteCart(productId, Integer.valueOf(userIdStr)));
    }

    /**
     *  批量勾选/取消勾选商品
     * @param productIds
     * @return
     */
    @PostMapping("/batchSelect")
    public Result batchSelect(@RequestParam("productIds") Integer[] productIds, @RequestParam("selected") Integer selected,
                              @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(cartService.batchSelect(productIds, selected, Integer.valueOf(userIdStr)));
    }

    /**
     *   全部或全不选（勾选）
     * @param selected
     * @return
     */
    @PostMapping("/selectAll")
    public Result selectAll(@RequestParam("selected") Integer selected, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(cartService.selectAllOrNot(selected, Integer.valueOf(userIdStr)));
    }
}
