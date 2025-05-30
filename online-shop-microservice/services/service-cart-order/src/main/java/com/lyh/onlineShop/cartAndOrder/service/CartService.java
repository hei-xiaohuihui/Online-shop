package com.lyh.onlineShop.cartAndOrder.service;

import com.lyh.onlineShop.cartAndOrder.dto.CartAddDto;
import com.lyh.onlineShop.cartAndOrder.dto.CartUpdateDto;
import com.lyh.onlineShop.cartAndOrder.vo.CartVo;

import java.util.List;

public interface CartService {

    List<CartVo> addCart(CartAddDto cartAddDto, Integer userId);

    List<CartVo> getCart(Integer userId);

    List<CartVo> updateCart(CartUpdateDto cartUpdateDto, Integer userId);

    List<CartVo> deleteCart(Integer productId, Integer userId);

    List<CartVo> batchSelect(Integer[] productIds, Integer selected, Integer userId);

    List<CartVo> selectAllOrNot(Integer selected, Integer userId);

    List<CartVo> checkCart(Integer userId);
}
