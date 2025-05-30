package com.lyh.onlineShop.productAndCategory.mapper;

import com.lyh.onlineShop.common.entity.Category;
import com.lyh.onlineShop.productAndCategory.dto.CategoryPageAdminDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Category selectByName(String name);

    int addCategory(Category category);

    int updateCategoryByCondition(Category category);

    Category selectById(Integer id);

    int deleteCategory(Integer id);

    List<Category> selectByCondition(CategoryPageAdminDto categoryPageAdminDto);

    List<Category> selectByParentId(Integer parentId);
}
