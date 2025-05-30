package com.lyh.onlineShop.productAndCategory.controller.admin;

import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.productAndCategory.dto.CategoryAddDto;
import com.lyh.onlineShop.productAndCategory.dto.CategoryPageAdminDto;
import com.lyh.onlineShop.productAndCategory.dto.CategoryUpdateDto;
import com.lyh.onlineShop.productAndCategory.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyh
 *  商品类别控制器——管理端
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     *  新增商品类别
     * @param categoryAddDto
     * @return
     */
    @PostMapping("/add")
    public Result addCategory(@Valid @RequestBody CategoryAddDto categoryAddDto) {
        categoryService.addCategory(categoryAddDto);
        return Result.success();
    }

    /**
     *  更新分类信息
     * @param categoryUpdateDto
     * @return
     */
    @PostMapping("/update")
    public Result updateCategory(@Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        categoryService.updateCategory(categoryUpdateDto);
        return Result.success();
    }

    /**
     *  删除分类信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteCategory(@RequestParam Integer id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     *  分页查询——管理端
     * @param categoryPageAdminDto
     * @return
     */
    @GetMapping("/page")
    public Result pageQueryForAdmin(CategoryPageAdminDto categoryPageAdminDto) { // 不是Json格式，不加@RequestBody
        return Result.success(categoryService.pageQueryForAdmin(categoryPageAdminDto));
    }
}
