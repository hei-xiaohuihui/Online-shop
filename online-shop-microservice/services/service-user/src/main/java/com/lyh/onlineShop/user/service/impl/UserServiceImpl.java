package com.lyh.onlineShop.user.service.impl;

import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import com.lyh.onlineShop.common.exception.BaseException;
import com.lyh.onlineShop.common.properties.JwtProperties;
import com.lyh.onlineShop.common.utils.MD5Util;
import com.lyh.onlineShop.user.dto.UserLoginDto;
import com.lyh.onlineShop.user.mapper.UserMapper;
import com.lyh.onlineShop.user.service.UserService;
import com.lyh.onlineShop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * @author lyh
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private JwtProperties jwtProperties;

    /**
     *  用户注册
     * @param userLoginDto
     */
    @Override
    public void register(UserLoginDto userLoginDto) {
        // 检查当前用户名是否存在
        User user = userMapper.selectByName(userLoginDto.getUsername());
        if(user != null) {
            throw new BaseException(ExceptionEnum.USER_EXIST);
        }

        // 对用户密码进行MD5加密
        try {
            userLoginDto.setPassword(MD5Util.getMD5Str(userLoginDto.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // 新增用户
        int insertResult = userMapper.addUser(userLoginDto);
        if(insertResult == 0) {
            throw new BaseException(ExceptionEnum.DB_INSERT_FAILED);
        }
    }

    /**
     * 用户登录
     *
     * @param userLoginDto
     * @return
     */
    @Override
    public User login(UserLoginDto userLoginDto) {
        String md5Password = null;
        try {
            md5Password = MD5Util.getMD5Str(userLoginDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        userLoginDto.setPassword(md5Password);
        User user = userMapper.selectByCondition(userLoginDto);
        if(user == null) {
            throw new BaseException(ExceptionEnum.USER_NAME_OR_PASSWORD_ERROR);
        }

        return user;
//        // 生成Jwt并返回
//        Map<String, Object> dataMap = new HashMap<>();
//        // 添加自定义信息到token中
//        dataMap.put(JwtClaimsConstant.USER_ID, user.getId()); // 添加用户id
//        dataMap.put(JwtClaimsConstant.USER_NAME, user.getUsername()); // 添加用户名
//        dataMap.put(JwtClaimsConstant.USER_ROLE, user.getRole()); // 添加用户角色信息
//        // 生成当前登录用户的Jwt并返回
//        return JwtUtil.generateToken(jwtProperties.getSecretKey(), jwtProperties.getExpireTime(), dataMap);
    }

    /**
     *  更新用户信息
     * @param user
     */
    @Override
    public void updateUser(User user) {
        if(user.getPassword() != null) {
            try {
                user.setPassword(MD5Util.getMD5Str(user.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        int updateResult = userMapper.updateUser(user);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
    }
}
