package com.lyh.onlineShop.productAndCategory.service;

import com.github.pagehelper.PageInfo;
import com.lyh.onlineShop.productAndCategory.dto.CategoryAddDto;
import com.lyh.onlineShop.productAndCategory.dto.CategoryPageAdminDto;
import com.lyh.onlineShop.productAndCategory.dto.CategoryUpdateDto;
import com.lyh.onlineShop.productAndCategory.vo.CategoryPageUserVo;

import java.util.List;

public interface CategoryService {

    void addCategory(CategoryAddDto categoryAddDto);

    void updateCategory(CategoryUpdateDto categoryUpdateDto);

    void deleteCategory(Integer id);

    PageInfo pageQueryForAdmin(CategoryPageAdminDto categoryPageAdminDto);

    List<CategoryPageUserVo> pageQueryForUser(Integer parentId);
}
