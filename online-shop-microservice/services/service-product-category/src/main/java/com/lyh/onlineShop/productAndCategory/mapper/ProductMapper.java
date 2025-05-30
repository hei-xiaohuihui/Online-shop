package com.lyh.onlineShop.productAndCategory.mapper;

import com.lyh.onlineShop.common.entity.Product;
import com.lyh.onlineShop.productAndCategory.dto.ProductAddDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductPageAdminDto;
import com.lyh.onlineShop.productAndCategory.query.ProductListQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    Product selectByName(String name);

    int addProduct(ProductAddDto productAddDto);

    int updateProductByCondition(Product product);

    Product selectById(Integer id);

    int deleteProduct(Integer id);

    int updateProductStatus(Integer[] ids, Integer newStatus);

    List<Product> selectByConditionForAdmin(ProductPageAdminDto productPageAdminDto);

    List<Product> selectByConditionForUser(ProductListQuery productListQuery);
}
