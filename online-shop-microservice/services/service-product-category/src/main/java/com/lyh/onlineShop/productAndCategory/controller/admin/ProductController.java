package com.lyh.onlineShop.productAndCategory.controller.admin;

import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.productAndCategory.dto.ProductAddDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductPageAdminDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductUpdateDto;
import com.lyh.onlineShop.productAndCategory.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lyh
 *  商品控制器——管理端
 */
@RestController("adminProductController")
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     *  新增商品
     * @param productAddDto
     * @return
     */
    @PostMapping("/add")
    public Result addProduct(@Valid @RequestBody ProductAddDto productAddDto) {
        productService.addProduct(productAddDto);
        return Result.success();
    }

    /**
     * 上传商品图片
     * @param request
     * @param file
     * @return
     */
    @PostMapping("/uploadImage")
    public Result uploadImage(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String imageUrl = productService.uploadImage(request, file);
        return Result.success(imageUrl);
    }

    /**
     * 更新商品信息
     * @param productUpdateDto
     * @return
     */
    @PostMapping("/update")
    public Result updateProduct(@Valid @RequestBody ProductUpdateDto productUpdateDto) {
        productService.updateProduct(productUpdateDto);
        return Result.success();
    }

    /**
     * 删除商品
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteProduct(@RequestParam Integer id) {
        productService.deleteProduct(id);
        return Result.success();
    }

    /**
     * 商品批量上下架
     * @param ids
     * @param newStatus
     * @return
     */
    @PostMapping("/status")
    public Result updateStatus(@RequestParam Integer[] ids, @RequestParam Integer newStatus) {
        productService.updateStatus(ids, newStatus);
        return Result.success();
    }

    /**
     * 商品分页查询（管理端）
     * @param productPageAdminDto
     * @return
     */
    @GetMapping("/page")
    public Result pageQuery(ProductPageAdminDto productPageAdminDto) { // 不是Json格式，不加@RequestBody注解
        return Result.success(productService.pageQueryForAdmin(productPageAdminDto));
    }

    /**
     *  获取商品详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public Result getProductDetail(@RequestParam Integer id) {
        return Result.success(productService.getProductDetail(id));
    }

}
