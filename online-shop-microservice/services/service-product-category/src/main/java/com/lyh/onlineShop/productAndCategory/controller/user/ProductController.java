package com.lyh.onlineShop.productAndCategory.controller.user;

import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.productAndCategory.dto.ProductPageUserDto;
import com.lyh.onlineShop.productAndCategory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyh
 *  商品控制器——用户端
 */
@RestController("userProductController")
@RequestMapping("/user/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     *  查询商品详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public Result getProductDetail(@RequestParam("id") Integer id) {
        return Result.success(productService.getProductDetail(id));
    }

    /**
     *  商品分页查询——用户端
     * @param productPageUserDto
     * @return
     */
    @GetMapping("/page")
    public Result pageQuery(ProductPageUserDto productPageUserDto) {
        return Result.success(productService.pageQueryForUser(productPageUserDto));
    }

}
