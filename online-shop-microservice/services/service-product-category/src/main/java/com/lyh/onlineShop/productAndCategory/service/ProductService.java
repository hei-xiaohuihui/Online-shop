package com.lyh.onlineShop.productAndCategory.service;

import com.github.pagehelper.PageInfo;
import com.lyh.onlineShop.common.entity.Product;
import com.lyh.onlineShop.productAndCategory.dto.ProductAddDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductPageAdminDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductPageUserDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    void addProduct(ProductAddDto productAddDto);

    String uploadImage(HttpServletRequest request, MultipartFile file);

    void updateProduct(ProductUpdateDto productUpdateDto);

    void deleteProduct(Integer id);

    void updateStatus(Integer[] ids, Integer newStatus);

    PageInfo pageQueryForAdmin(ProductPageAdminDto productPageAdminDto);

    Product getProductDetail(Integer id);

    PageInfo pageQueryForUser(ProductPageUserDto productListDto);
}
