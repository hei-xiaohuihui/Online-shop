package com.lyh.onlineShop.common.entity;

import lombok.Data;

/**
 * @author lyh
 *  用户地址簿实体类
 */
@Data
public class AddressBook {

    private Integer id;

    private Integer userId;

    private String consignee;

    private Integer sex;

    private String phone;

    private String address;

    private String label;

    private Integer defaulted;
}
