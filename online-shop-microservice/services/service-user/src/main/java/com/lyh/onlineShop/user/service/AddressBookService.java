package com.lyh.onlineShop.user.service;

import com.lyh.onlineShop.user.dto.AddressAddDto;
import com.lyh.onlineShop.user.dto.AddressUpdateDto;
import com.lyh.onlineShop.user.vo.AddressVo;

import java.util.List;

public interface AddressBookService {


    void addAddressBook(AddressAddDto addressAddDto, Integer userId);

    List<AddressVo> getAddressBookList(Integer userId);

    AddressVo getAddressBookById(Integer id);

    void updateAddressBook(AddressUpdateDto addressUpdateDto, Integer userId);

    void deleteAddressBookById(Integer id);
}
