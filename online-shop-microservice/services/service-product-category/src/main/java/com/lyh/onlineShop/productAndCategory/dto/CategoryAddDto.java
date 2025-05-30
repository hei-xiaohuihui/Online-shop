package com.lyh.onlineShop.productAndCategory.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author lyh
 *  新增商品分类数据传输对象
 */
@Data
public class CategoryAddDto {

    @Size(min = 2, max = 5, message = "分类名称长度必须在2-5之间")
    @NotNull(message = "分类名称不能为空")
    private String name;

    @Max(value = 3, message = "分类目录级别为1-3级")
    @NotNull(message = "分类目录级别不能为空")
    private Integer type;

    @NotNull(message = "父级分类不能为空")
    private Integer parentId;

    @NotNull(message = "排序不能为空")
    private Integer orderNum;
}
