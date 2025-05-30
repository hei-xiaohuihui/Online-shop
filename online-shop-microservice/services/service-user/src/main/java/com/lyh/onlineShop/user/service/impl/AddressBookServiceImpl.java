package com.lyh.onlineShop.user.service.impl;

import com.lyh.onlineShop.common.entity.AddressBook;
import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import com.lyh.onlineShop.common.exception.BaseException;
import com.lyh.onlineShop.constant.FlagConstant;
import com.lyh.onlineShop.user.dto.AddressAddDto;
import com.lyh.onlineShop.user.dto.AddressUpdateDto;
import com.lyh.onlineShop.user.mapper.AddressBookMapper;
import com.lyh.onlineShop.user.service.AddressBookService;
import com.lyh.onlineShop.user.vo.AddressVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyh
 */
@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressAddDto
     * @param userId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAddressBook(AddressAddDto addressAddDto, Integer userId) {
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressAddDto, addressBook);
        addressBook.setUserId(userId);
        // 新增的地址默认设置为默认地址
        List<AddressBook> addressBookList = addressBookMapper.getAddressBookList(userId);
        // 在此处查询用户的其他地址，如果存在设置为非默认地址
        setOtherAddressBookNotDefaulted(addressBookList);
        // 新增地址
        addressBookMapper.addAddressBook(addressBook);
    }

    /**
     * 获取当前用户的地址列表
     * @param userId
     * @return
     */
    @Override
    public List<AddressVo> getAddressBookList(Integer userId) {
        // 查询用户地址簿信息，并判断是否为空
        List<AddressBook> addressBookList = addressBookMapper.getAddressBookList(userId);
        if(CollectionUtils.isEmpty(addressBookList)) {
            throw new BaseException(ExceptionEnum.ADDRESS_BOOK_NOT_ADD); // 抛出用户未添加地址信息业务异常
        }

        // 封装成vo
        List<AddressVo> addressBookVoList = new ArrayList<>();
        for(AddressBook addressBook : addressBookList) {
            AddressVo addressBookVo = new AddressVo();
            BeanUtils.copyProperties(addressBook, addressBookVo);
            addressBookVoList.add(addressBookVo);
        }
        return addressBookVoList;
    }

    /**
     *  根据id查询地址详情
     * @param id
     * @return
     */
    @Override
    public AddressVo getAddressBookById(Integer id) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        if(addressBook == null) {
            throw new BaseException(ExceptionEnum.ADDRESS_BOOK_NOT_EXIST);
        }

        // 封装成vo
        AddressVo addressBookVo = new AddressVo();
        BeanUtils.copyProperties(addressBook, addressBookVo);
        return addressBookVo;
    }

    /**
     * 修改地址信息
     *
     * @param addressUpdateDto
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAddressBook(AddressUpdateDto addressUpdateDto, Integer userId) {
        // 封装成实体对象
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressUpdateDto, addressBook);
        addressBook.setUserId(userId);

        // 判断是否更新为默认地址
        if(addressBook.getDefaulted() == FlagConstant.FLAG_TRUE) {
            // 获取当前用户的默认地址
            AddressBook oldDefaultAddress = addressBookMapper.getDefaultAddress(userId);
            if(oldDefaultAddress != null && !addressBook.equals(oldDefaultAddress)) { // 如果存在默认地址且不是当前用户的默认地址
                // 将旧默认地址设置为非默认地址
                oldDefaultAddress.setDefaulted(FlagConstant.FLAG_FALSE);
                addressBookMapper.updateByCondition(oldDefaultAddress);
            }
        }
        // 更新地址
        addressBookMapper.updateByCondition(addressBook);
    }

    /**
     *  删除某条地址信息
     * @param id
     */
    @Override
    public void deleteAddressBookById(Integer id) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        if(addressBook == null) {
            throw new BaseException(ExceptionEnum.ADDRESS_BOOK_NOT_EXIST);
        }

        int deleteResult = addressBookMapper.deleteById(id);
        if(deleteResult == 0) {
            throw new BaseException(ExceptionEnum.DB_DELETE_FAILED);
        }
    }

    /*
        设置其他地址问非默认地址辅助方法
     */
    private void setOtherAddressBookNotDefaulted(List<AddressBook> addressBookList) {
        if(!CollectionUtils.isEmpty(addressBookList)) { // 如果存在其他地址
            for(AddressBook address : addressBookList) {
                if(address.getDefaulted() == FlagConstant.FLAG_TRUE) { // 如果地址为默认地址，则设置为非默认地址
                    address.setDefaulted(FlagConstant.FLAG_FALSE);
                    addressBookMapper.updateByCondition(address);
                }
            }
        }
    }
}
