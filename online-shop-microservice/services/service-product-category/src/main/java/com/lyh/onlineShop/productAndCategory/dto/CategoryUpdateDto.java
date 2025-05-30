package com.lyh.onlineShop.productAndCategory.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author lyh
 *  更新商品分类数据传输对象
 */
@Data
public class CategoryUpdateDto {

    @NotNull(message = "分类id不能为空")
    private Integer id;

    private String name;

    @Max(value = 3, message = "分类目录级别为1-3级")
    private Integer type;

    private Integer parentId;

    private Integer orderNum;
}
