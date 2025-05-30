package com.lyh.onlineShop.cartAndOrder.feign;

import com.lyh.onlineShop.common.entity.AddressBook;
import com.lyh.onlineShop.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *  用户模块的Feign客户端，用于调用用户模块的地址簿API
 */
@FeignClient(value = "service-user")
public interface AddressFeignClient {

    /**
     *  根据id获取地址详情
     * @param id
     * @return
     */
    @GetMapping("/auth/addressBook/{id}")
    Result<AddressBook> getAddressBook(@PathVariable("id") Integer id);
}
