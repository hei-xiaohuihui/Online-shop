package com.lyh.onlineShop.productAndCategory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lyh
 *  新增商品数据传输对象
 */
@Data
public class ProductAddDto {

    @NotNull(message = "商品名称不能为空")
    private String name;

    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能小于0")
    private BigDecimal  price;

    @NotNull(message = "商品分类不能为空")
    private Integer categoryId;

    private String description;

    private String image;

    @NotNull(message = "商品库存不能为空")
    @Min(value = 0, message = "商品库存不能小于0")
    private Integer stock;

    private Integer status;
}
