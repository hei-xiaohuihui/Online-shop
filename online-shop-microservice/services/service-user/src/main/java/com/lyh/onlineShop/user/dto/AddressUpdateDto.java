package com.lyh.onlineShop.user.dto;

import lombok.Data;

/**
 * @author lyh
 *  用户地址簿更新数据传输对象
 */
@Data
public class AddressUpdateDto {

    private Integer id;

    private String consignee;

    private Integer sex;

    private String phone;

    private String address;

    private String label;

    private Integer defaulted;
}
