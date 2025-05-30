package com.lyh.onlineShop.productAndCategory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lyh
 *  商品更新数据传输对象
 */
@Data
public class ProductUpdateDto {

    @NotNull(message = "商品id不能为空")
    private Integer id;

    private String name;

    private BigDecimal price;

    private Integer categoryId;

    private String description;

    private String image;

    private Integer stock;

    private Integer status;
}
