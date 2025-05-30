package com.lyh.onlineShop.productAndCategory.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyh.onlineShop.common.entity.Product;
import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import com.lyh.onlineShop.common.exception.BaseException;
import com.lyh.onlineShop.constant.OrderByConstant;
import com.lyh.onlineShop.productAndCategory.dto.ProductAddDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductPageAdminDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductPageUserDto;
import com.lyh.onlineShop.productAndCategory.dto.ProductUpdateDto;
import com.lyh.onlineShop.productAndCategory.mapper.ProductMapper;
import com.lyh.onlineShop.productAndCategory.query.ProductListQuery;
import com.lyh.onlineShop.productAndCategory.service.CategoryService;
import com.lyh.onlineShop.productAndCategory.service.ProductService;
import com.lyh.onlineShop.productAndCategory.vo.CategoryPageUserVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author lyh
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;

    @Value("${lyh.file.image.path}")
    private String imageUploadPath;

    /**
     *  添加商品
     * @param productAddDto
     */
    @Override
    public void addProduct(ProductAddDto productAddDto) {
        // 先检查商品是否存在
        Product product = productMapper.selectByName(productAddDto.getName());
        if(product != null) {
            throw new BaseException(ExceptionEnum.PRODUCT_EXIST);
        }

        int insertResult = productMapper.addProduct(productAddDto);
        if(insertResult == 0) {
            throw new BaseException(ExceptionEnum.DB_INSERT_FAILED);
        }
    }

    /**
     *  上传商品图片
     * @param request
     * @param file
     * @return
     */
    @Override
    public String uploadImage(HttpServletRequest request, MultipartFile file) {
        // 检查上传的图片文件是否为空
        if(file == null || file.isEmpty()) {
            throw new BaseException(ExceptionEnum.IMAGE_UPLOAD_EMPTY);
        }

        // 获取文件名（文件后缀）
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".")); // 获取文件后缀
//        log.info("文件后缀{}", suffix);

        // 利用UUID生成新的文件名
        String newFileName = UUID.randomUUID() + suffix; // 新的文件名
//        log.info("新文件名{}", newFileName);

        // 检查保存图片的目录是否存在，不存在则创建
        File uploadDir = new File(imageUploadPath);
        if(!uploadDir.exists() && !uploadDir.mkdirs()) { // 文件创建失败
            throw new BaseException(ExceptionEnum.SYSTEM_FILE_CREATE_FAILED);
        }

        // 保存文件
        File destination = new File(uploadDir, newFileName);
        try {
            file.transferTo(destination);
        } catch (IOException e) {
            log.error("图片上传失败", e);
            throw new BaseException(ExceptionEnum.IMAGE_UPLOAD_FAILED);
        }

        // 构建文件访问路径
        try {
            String url = getHost(new URI(request.getRequestURL() + "")) + "/images/" + newFileName;
            log.info("文件上传成功，访问路径{}", url);
            return url;
        } catch (URISyntaxException e) {
            throw new BaseException(ExceptionEnum.IMAGE_UPLOAD_FAILED);
        }
    }

    /**
     *  更新商品信息
     * @param productUpdateDto
     */
    @Override
    public void updateProduct(ProductUpdateDto productUpdateDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productUpdateDto, product);
        // 若要修改商品名称，则需要判断该名称商品是否已存在
        if(product.getName() != null) {
            Product oldProduct = productMapper.selectByName(product.getName());
            if(oldProduct != null && !oldProduct.getId().equals(product.getId())) {
                throw new BaseException(ExceptionEnum.PRODUCT_EXIST);
            }
        }

        int updateResult = productMapper.updateProductByCondition(product);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
    }

    /**
     *  删除商品
     * @param id
     */
    @Override
    public void deleteProduct(Integer id) {
        Product product = productMapper.selectById(id);
        if(product == null) {
            throw new BaseException(ExceptionEnum.PRODUCT_NOT_EXIST);
        }

        int deleteResult = productMapper.deleteProduct(id);
        if(deleteResult == 0) {
            throw new BaseException(ExceptionEnum.DB_DELETE_FAILED);
        }
    }

    /**
     *  批量更新商品状态
     * @param ids
     * @param newStatus
     */
    @Override
    public void updateStatus(Integer[] ids, Integer newStatus) {
        int batchUpdateResult = productMapper.updateProductStatus(ids, newStatus);
        if (batchUpdateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
    }

    /**
     *  分页查询商品信息——管理端
     * @param productPageAdminDto
     * @return
     */
    @Override
    public PageInfo pageQueryForAdmin(ProductPageAdminDto productPageAdminDto) {
        // 开启分页
        PageHelper.startPage(productPageAdminDto.getPageNum(), productPageAdminDto.getPageSize());
        // 查询商品
        List<Product> productList = productMapper.selectByConditionForAdmin(productPageAdminDto);
        // 封装分页信息并返回
        return new PageInfo(productList);
    }

    /**
     *  分页查询商品信息——用户端
     * @param productPageUserDto
     * @return
     */
    @Override
    public PageInfo pageQueryForUser(ProductPageUserDto productPageUserDto) {
        // 创建查询条件对象
        ProductListQuery productListQuery = new ProductListQuery();
        // 如果商品名称不为空，则设置名称查询条件
        if(productPageUserDto.getName() != null) {
            productListQuery.setName(productPageUserDto.getName());
        }

        // 如果商品分类id不为空，则需要查询该分类及其子分类下的所有商品信息
        if(productPageUserDto.getCategoryId() != null) {
            // 先查询出该分类下的所有子分类信息
            List<CategoryPageUserVo> childrenList = categoryService.pageQueryForUser(productPageUserDto.getCategoryId());
            // 记录所有需要查查询的categoryId的列表
            List<Integer> categoryIdsList = new ArrayList<>();
            // 将当前分类的categoryId先添加到列表中
            categoryIdsList.add(productPageUserDto.getCategoryId());
            // 递归查询该分类下的所有子分类id并加入categoryIdsList列表中
            recursionGetCategoryIds(categoryIdsList, childrenList);

            // 设置查询对象productListQuery的categoryIds列表属性
            productListQuery.setCategoryIds(categoryIdsList);
        }

        // 判断是否传入了受支持排序规则
        if(productPageUserDto.getOrderBy() != null) { // 若传入了支持的排序规则，则开启分页时设置排序规则
            if(OrderByConstant.ORDER_BY_SET.contains(productPageUserDto.getOrderBy())) {
                PageHelper.startPage(productPageUserDto.getPageNum(), productPageUserDto.getPageSize(), productPageUserDto.getOrderBy());
            }
        }else { // 若传入的排序规则不受支持或未传入，则直接开启分页
            PageHelper.startPage(productPageUserDto.getPageNum(), productPageUserDto.getPageSize());
        }

        // 根据查询条件对象查询所有商品信息
        List<Product> productList = productMapper.selectByConditionForUser(productListQuery);
        // 封装分页信息并返回
        return new PageInfo(productList);
    }

    /**
     *  获取商品详情
     * @param id
     * @return
     */
    @Override
    public Product getProductDetail(Integer id) {
        Product product = productMapper.selectById(id);
        if(product == null) {
            throw new BaseException(ExceptionEnum.PRODUCT_NOT_EXIST);
        }
        return product;
    }


    /*
        递归获取分类列表中所有分类及其子分类的id
     */
    private void recursionGetCategoryIds(List<Integer> idsList, List<CategoryPageUserVo> categoryPageUserVoList) {
        // 递归终止条件（当categoryList为空时，递归结束）
        if(CollectionUtils.isEmpty(categoryPageUserVoList)) {
            return;
        }

        // 遍历categoryList
        for(CategoryPageUserVo categoryPageUserVo : categoryPageUserVoList) {
            idsList.add(categoryPageUserVo.getId()); // 先将当前分类的id加入结果列表中
            recursionGetCategoryIds(idsList, categoryPageUserVo.getChildren()); // 再递归当前分类下的子分类
        }
    }

    /*
        获取服务器地址辅助办法
     */
    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null; // 新建失败了就返回null
        }
        return effectiveURI;
    }
}
