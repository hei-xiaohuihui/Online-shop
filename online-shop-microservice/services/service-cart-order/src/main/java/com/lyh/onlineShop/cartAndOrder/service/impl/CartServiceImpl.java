package com.lyh.onlineShop.cartAndOrder.service.impl;

import com.lyh.onlineShop.cartAndOrder.dto.CartAddDto;
import com.lyh.onlineShop.cartAndOrder.dto.CartUpdateDto;
import com.lyh.onlineShop.cartAndOrder.feign.ProductFeignClient;
import com.lyh.onlineShop.cartAndOrder.vo.CartVo;
import com.lyh.onlineShop.cartAndOrder.mapper.CartMapper;
import com.lyh.onlineShop.cartAndOrder.service.CartService;
import com.lyh.onlineShop.common.entity.Cart;
import com.lyh.onlineShop.common.entity.Product;
import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import com.lyh.onlineShop.common.exception.BaseException;
import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.constant.FlagConstant;
import com.lyh.onlineShop.constant.ProductStatusConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyh
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductFeignClient productFeignClient; // 远程调用商品服务

    /**
     *  根据用户id获取其购物车列表
     * @param userId
     * @return
     */
    @Override
    public List<CartVo> getCart(Integer userId) {
        // 根据用户id获取购物车列表
        List<CartVo> cartVoList = cartMapper.selectByUserId(userId);
        // 计算每个商品的总价
        for(CartVo cartVo : cartVoList) {
            cartVo.setTotalPrice(cartVo.getPrice().multiply(BigDecimal.valueOf(cartVo.getQuantity())));
        }
        return cartVoList;
    }

    /**
     *  确认订单界面获取用户购物车中所有勾选的商品信息
     * @param userId
     * @return
     */
    @Override
    public List<CartVo> checkCart(Integer userId) {
        List<CartVo> cartVoList = getCart(userId);
        // 删选出被勾选的商品
        cartVoList.stream().filter(cartVo -> cartVo.getSelected().equals(FlagConstant.FLAG_TRUE)).collect(Collectors.toList());
        return cartVoList;
    }

    /**
     *  向购物车中添加商品
     * @param cartAddDto
     * @param userId
     * @return
     */
    @Override
    public List<CartVo> addCart(CartAddDto cartAddDto, Integer userId) {
        // 首先要检查要添加的商品是否在售/商品库存是否充足
        // 从数据库查询商品信息
        Result<Product> result = productFeignClient.getProductDetail(cartAddDto.getProductId());
//        Product product = new Product();
//        if(result != null && result.getCode().equals(Result.SUCCESS_CODE)) {
//            product = result.getData();
//        } else{
//            throw new BaseException(ExceptionEnum.REMOTE_CALL_ERROR); // 远程调用失败
//        }
        isRPCSuccess(result);
        Product product = result.getData();

        // 检查商品是否在售/库存是否充足
        checkProduct(product, cartAddDto.getCount());

        // 检查该商品是否已存在购物车中
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, cartAddDto.getProductId());
        // 情况一: 商品在购车中已存在，更新购物车中商品的数量即可
        if(cart != null) {
            if(cart.getQuantity() + cartAddDto.getCount() > product.getStock()) {
                throw new BaseException(ExceptionEnum.PRODUCT_NOT_ENOUGH); // 商品库存不足
            }
            // 直接更新购物车信息
            cart.setQuantity(cart.getQuantity() + cartAddDto.getCount());
            int updateResult = cartMapper.updateCartByCondition(cart);
            if(updateResult == 0) {
                throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
            }
        }else { // 情况二: 商品在购物车中不存在
            cart = Cart.builder()
                    .userId(userId) // 用户id
                    .productId(cartAddDto.getProductId()) //  商品id
                    .quantity(cartAddDto.getCount()) // 商品数量
                    .selected(FlagConstant.FLAG_TRUE) // 新加入购物车的商品默认为勾选状态
                    .build();
            // 插入新增信息到购物车表单中
            int insertResult = cartMapper.addCart(cart);
            if(insertResult == 0) {
                throw new BaseException(ExceptionEnum.DB_INSERT_FAILED);
            }
        }

        // 返回更新后的购物车列表
        return getCart(userId);
    }

    /**
     *  更新购物车商品信息
     * @param cartUpdateDto
     * @param userId
     * @return
     */
    @Override
    public List<CartVo> updateCart(CartUpdateDto cartUpdateDto, Integer userId) {
        // 首先要检查商品是否存在/是否在售/库存是否充足
        // 先从数据库中查询商品信息
        Result<Product> result = productFeignClient.getProductDetail(cartUpdateDto.getProductId());
        isRPCSuccess(result);
        Product product = result.getData();
        // 检查商品是否存在/是否在售/库存是否充足
        checkProduct(product, cartUpdateDto.getQuantity());

        // 检查购物车中是否存在该商品（根据用户id和商品id查询是否存在对应的购物车信息）
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, cartUpdateDto.getProductId());
        if(cart == null) {
            throw new BaseException(ExceptionEnum.CART_NOT_EXIST);
        }

        if(cartUpdateDto.getQuantity() != null) {
            cart.setQuantity(cartUpdateDto.getQuantity());
        }
        if(cartUpdateDto.getSelected() != null) {
            cart.setSelected(cartUpdateDto.getSelected());
        }

        // 更新购物车信息
        int updateResult = cartMapper.updateCartByCondition(cart);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }

        // 返回更新后的购物车列表
        return getCart(userId);
    }

    /**
     *  删除购物车商品信息
     * @param productId
     * @param userId
     * @return
     */
    @Override
    public List<CartVo> deleteCart(Integer productId, Integer userId) {
        // 检查购物车信息是否存在
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if(cart == null) {
            throw new BaseException(ExceptionEnum.CART_NOT_EXIST);
        }else {
            int deleteResult = cartMapper.deleteCart(cart.getId());
            if(deleteResult == 0) {
                throw new BaseException(ExceptionEnum.DB_DELETE_FAILED);
            }
        }
        // 返回删除后的购物车列表
        return getCart(userId);
    }

    /**
     *  批量勾选购物车商品
     * @param productIds
     * @param selected
     * @param userId
     * @return
     */
    @Override
    public List<CartVo> batchSelect(Integer[] productIds, Integer selected, Integer userId) {
        int updateResult = cartMapper.updateCartStatus(userId, productIds, selected);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
        return getCart(userId);
    }

    /**
     *  全选或者全不选购物车商品
     * @param selected
     * @param userId
     * @return
     */
    @Override
    public List<CartVo> selectAllOrNot(Integer selected, Integer userId) {
        int updateResult = cartMapper.updateCartStatus(userId, null, selected);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
        return getCart(userId);
    }

    /*
        检查商品状态和库存的辅助方法
     */
    private void checkProduct(Product product, Integer count) {
        if(product == null) {
            throw new BaseException(ExceptionEnum.PRODUCT_NOT_EXIST); // 商品不存在
        }

        if(product.getStatus() == ProductStatusConstant.PRODUCT_STATUS_OFF) {
            throw new BaseException(ExceptionEnum.PRODUCT_NOT_AVAILABLE); // 商品已下架
        }

        if(product.getStock() < count) {
            throw new BaseException(ExceptionEnum.PRODUCT_NOT_ENOUGH); // 商品库存不足
        }
    }

    /*
        检查远程调用是否成功
     */
    private void isRPCSuccess(Result result) {
        if(result == null || !result.getCode().equals(Result.SUCCESS_CODE)) {
            throw new BaseException(ExceptionEnum.REMOTE_CALL_ERROR);
        }
    }
}
