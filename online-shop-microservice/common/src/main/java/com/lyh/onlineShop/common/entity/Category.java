package com.lyh.onlineShop.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author lyh
 *  商品分类实体类
 */
@Data
public class Category {

    private Integer id;

    private String name;

    // 分类目录级别，1：一级目录，2：二级目录，3：三级目录
    private Integer type;

    // 父级目录id，一级目录的parentId为0
    private Integer parentId;

    // 在同级目录下的排序
    private Integer orderNum;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
