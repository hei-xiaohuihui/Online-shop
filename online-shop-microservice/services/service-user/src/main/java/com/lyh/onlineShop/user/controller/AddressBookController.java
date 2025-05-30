package com.lyh.onlineShop.user.controller;

import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.constant.JwtClaimsConstant;
import com.lyh.onlineShop.user.dto.AddressAddDto;
import com.lyh.onlineShop.user.dto.AddressUpdateDto;
import com.lyh.onlineShop.user.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyh
 */
@RestController
@RequestMapping("/auth/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressAddDto
     * @return
     */
    @PostMapping("/add")
    public Result addAddressBook(@RequestBody AddressAddDto addressAddDto, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        addressBookService.addAddressBook(addressAddDto, Integer.valueOf(userIdStr));
        return Result.success();
    }

    /**
     *  获取用户地址列表
     * @return
     */
    @GetMapping("/list")
    public Result getAddressBookList(@RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        return Result.success(addressBookService.getAddressBookList(Integer.valueOf(userIdStr)));
    }

    /**
     *  根据id查询地址详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getAddressBook(@PathVariable("id") Integer id) {
        return Result.success(addressBookService.getAddressBookById(id));
    }

    /**
     *  修改地址
     * @param addressUpdateDto
     * @return
     */
    @PostMapping("/update")
    public Result updateAddressBook(@RequestBody AddressUpdateDto addressUpdateDto, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        addressBookService.updateAddressBook(addressUpdateDto, Integer.valueOf(userIdStr));
        return Result.success();
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteAddressBook(@RequestParam("id") Integer id) {
        addressBookService.deleteAddressBookById(id);
        return Result.success();
    }
}
