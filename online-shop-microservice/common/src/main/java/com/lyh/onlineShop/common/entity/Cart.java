package com.lyh.onlineShop.common.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lyh
 *  购物车实体类
 */
@Data
@Builder
public class Cart {

    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer selected;

    private LocalDateTime  createTime;

    private LocalDateTime  updateTime;
}
