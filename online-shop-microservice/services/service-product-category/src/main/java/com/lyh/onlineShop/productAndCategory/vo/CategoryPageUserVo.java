package com.lyh.onlineShop.productAndCategory.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyh
 *  商品分页查询视图对象——用户端
 */
@Data
public class CategoryPageUserVo {

    private Integer id;

    private String name;

    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    private LocalDateTime  createTime;

    private LocalDateTime  updateTime;

    // 子分类列表
    private List<CategoryPageUserVo> children = new ArrayList<>();
}
