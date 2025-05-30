package com.lyh.onlineShop.cartAndOrder.feign;

import com.lyh.onlineShop.common.entity.Product;
import com.lyh.onlineShop.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  商品/商品分类模块的Feign客户端
 */
@FeignClient(value = "service-product-category") // 说明这是一个Feign客户端，用与调用service-product-category模块的API
public interface ProductFeignClient {

    /**
     *  根据id获取商品详情
     * @param id
     * @return
     */
    @GetMapping("/user/product/detail")
    Result<Product> getProductDetail(@RequestParam("id") Integer id);

    /**
     *  修改商品信息
     * @param product
     * @return
     */
    @PostMapping("/admin/product/update")
    Result updateProduct(@RequestBody Product product);

}
