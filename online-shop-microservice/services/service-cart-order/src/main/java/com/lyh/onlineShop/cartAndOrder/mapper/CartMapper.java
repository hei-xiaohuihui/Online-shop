package com.lyh.onlineShop.cartAndOrder.mapper;

import com.lyh.onlineShop.cartAndOrder.vo.CartVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.lyh.onlineShop.common.entity.Cart;

import java.util.List;

@Mapper
public interface CartMapper {

    /**
     *  根据用户id和商品id查询
     * @param userId
     * @param productId
     * @return
     */
    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     *  根据条件更新购物车商品信息
     * @param cart
     * @return
     */
//    @TimeAutoFill
    int updateCartByCondition(Cart cart);

    /**
     *  添加购物车商品信息
     * @param cart
     * @return
     */
    int addCart(Cart cart);

    /**
     *  根据用户id查询其购物车商品信息
     * @param userId
     * @return
     */
    List<CartVo> selectByUserId(Integer userId);

    /**
     *  根据主键id删除购物车商品信息
     * @param id
     * @return
     */
    int deleteCart(Integer id);

    /**
     *  批量更新购物车商品勾选状态
     * @param userId
     * @param productIds
     * @param selected
     * @return
     */
    int updateCartStatus(@Param("userId") Integer userId, @Param("productIds") Integer[] productIds, @Param("selected") Integer selected);
}
