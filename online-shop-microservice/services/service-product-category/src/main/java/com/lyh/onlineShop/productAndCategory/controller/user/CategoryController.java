package com.lyh.onlineShop.productAndCategory.controller.user;

import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.productAndCategory.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyh
 *  商品类别控制器——用户端
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     *  分页查询（用户端）
     * @return
     */
    @GetMapping("/page")
    public Result pageQueryForUser() {
        return Result.success(categoryService.pageQueryForUser(0)); // 一级目录的parentId默认为0
    }
}
