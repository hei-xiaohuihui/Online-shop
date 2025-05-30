package com.lyh.onlineShop.common.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lyh
 *  商品实体类
 */
@Data
public class Product {

    private Integer id;

    private String name;

    private BigDecimal price;

    private Integer categoryId;

    private String description;

    private String image;

    private Integer stock;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
