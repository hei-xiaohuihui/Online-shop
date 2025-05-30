package com.lyh.onlineShop.user.vo;

import lombok.Data;

/**
 * @author lyh
 *  用户地址信息视图对象
 */
@Data
public class AddressVo {

    private Integer id;

    private String consignee;

    private Integer sex;

    private String phone;

    private String address;

    private String label;

    private Integer defaulted;
}
