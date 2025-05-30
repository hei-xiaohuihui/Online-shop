package com.lyh.onlineShop.user.service;

import com.lyh.onlineShop.common.entity.User;
import com.lyh.onlineShop.user.dto.UserLoginDto;

public interface UserService {


    void register(UserLoginDto userLoginDto);

    User login(UserLoginDto userLoginDto);

    void updateUser(User user);
}
