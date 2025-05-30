package com.lyh.onlineShop.productAndCategory.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyh.onlineShop.common.entity.Category;
import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import com.lyh.onlineShop.common.exception.BaseException;
import com.lyh.onlineShop.productAndCategory.dto.CategoryAddDto;
import com.lyh.onlineShop.productAndCategory.dto.CategoryPageAdminDto;
import com.lyh.onlineShop.productAndCategory.dto.CategoryUpdateDto;
import com.lyh.onlineShop.productAndCategory.mapper.CategoryMapper;
import com.lyh.onlineShop.productAndCategory.service.CategoryService;
import com.lyh.onlineShop.productAndCategory.vo.CategoryPageUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyh
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     *  添加分类
     * @param categoryAddDto
     */
    @Override
    public void addCategory(CategoryAddDto categoryAddDto) {
        // 判断分类是否存在
        if(categoryMapper.selectByName(categoryAddDto.getName()) != null) {
            throw new BaseException(ExceptionEnum.CATEGORY_EXIST);
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryAddDto, category);
        int addResult = categoryMapper.addCategory(category);
        if(addResult == 0) {
            throw new BaseException(ExceptionEnum.DB_INSERT_FAILED);
        }
    }

    /**
     *  修改分类信息
     * @param categoryUpdateDto
     */
    @Override
    public void updateCategory(CategoryUpdateDto categoryUpdateDto) {
        // 若要修改分类的名称，则需要判断该名称是否已存在
        if(categoryUpdateDto.getName() != null) {
            Category oldCategory = categoryMapper.selectByName(categoryUpdateDto.getName());
            if(oldCategory != null && !oldCategory.getId().equals(categoryUpdateDto.getId())) { // 该名称分类已存在，且不是当前修改的分类
                throw new BaseException(ExceptionEnum.CATEGORY_EXIST);
            }
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryUpdateDto, category);
        int updateResult = categoryMapper.updateCategoryByCondition(category);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
    }

    /**
     *  删除分类
     * @param id
     */
    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryMapper.selectById(id);
        if(category == null) {
            throw new BaseException(ExceptionEnum.CATEGORY_NOT_EXIST);
        }
        int deleteResult = categoryMapper.deleteCategory(id);
        if(deleteResult == 0) {
            throw new BaseException(ExceptionEnum.DB_DELETE_FAILED);
        }
    }

    /**
     *  分页查询分类信息——管理端
     * @param categoryPageAdminDto
     * @return
     */
    @Override
    public PageInfo pageQueryForAdmin(CategoryPageAdminDto categoryPageAdminDto) {
        // 开启分页，并指定排序规则
        PageHelper.startPage(categoryPageAdminDto.getPageNum(), categoryPageAdminDto.getPageSize(), "type, order_num");
        // 查询分类信息，支持模糊查询和条件查询
        List<Category> categoryList = categoryMapper.selectByCondition(categoryPageAdminDto);
        // 封装分页信息并返回
        return new PageInfo(categoryList);
    }

    /**
     *  分页查询分类信息——用户端
     * @param parentId
     * @return
     */
    @Override
    public List<CategoryPageUserVo> pageQueryForUser(Integer parentId) {
        // 记录查询到的所有分类信息的列表
        List<CategoryPageUserVo> list = new ArrayList<>();
        // 递归查询分类信息及其子分类信息
        recursionFindCategories(list, parentId); // 一级目录的parentId默认是0
        return list;
    }

    /*
        递归查询分类信息及其子分类信息，并将查询结果添加到列表中
     */
    private void recursionFindCategories(List<CategoryPageUserVo> voList, Integer parentId) {
        // 查询当前parentId下的所有子分类信息
        List<Category> categoryList = categoryMapper.selectByParentId(parentId);
        // 递归终止条件（当当前parentId下无子分类时，递归结束）
        if(CollectionUtils.isEmpty(categoryList)) {
            return;
        }

        // 遍历当前parentId下的所有子分类
        for(Category category : categoryList) {
            // 将当前分类信息封装到vo中
            CategoryPageUserVo vo = new CategoryPageUserVo();
            BeanUtils.copyProperties(category, vo); // 拷贝属性
            // 将当前分类信息添加到结果列表中
            voList.add(vo);
            // 当前分类的id即为子类的parentId，递归查询子类信息，添加到该分类的结果列表中
            recursionFindCategories(vo.getChildren(), category.getId());
        }
    }
}
