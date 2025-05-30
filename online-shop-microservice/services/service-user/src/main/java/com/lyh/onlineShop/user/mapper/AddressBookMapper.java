package com.lyh.onlineShop.user.mapper;

import com.lyh.onlineShop.common.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper {


    int updateByCondition(AddressBook address);

    List<AddressBook> getAddressBookList(Integer userId);

    int addAddressBook(AddressBook addressBook);

    AddressBook selectById(Integer id);

    AddressBook getDefaultAddress(Integer userId);

    int deleteById(Integer id);
}
