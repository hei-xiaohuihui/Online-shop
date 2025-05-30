package com.lyh.onlineShop.user.dto;

import lombok.Data;

/**
 * @author lyh
 *  新增用户地址信息数据传输对象
 */
@Data
public class AddressAddDto {

    private String consignee;

    private Integer sex;

    private String phone;

    private String address;

    private String label;
}
