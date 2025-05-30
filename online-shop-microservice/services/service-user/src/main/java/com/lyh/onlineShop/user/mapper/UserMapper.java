package com.lyh.onlineShop.user.mapper;

import com.lyh.onlineShop.common.entity.User;
import com.lyh.onlineShop.user.dto.UserLoginDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectByName(String username);

    int addUser(UserLoginDto userLoginDto);

    User selectByCondition(UserLoginDto userLoginDto);

    int updateUser(User user);
}
