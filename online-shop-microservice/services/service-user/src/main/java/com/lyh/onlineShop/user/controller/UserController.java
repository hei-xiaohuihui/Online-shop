package com.lyh.onlineShop.user.controller;

import com.lyh.onlineShop.common.properties.JwtProperties;
import com.lyh.onlineShop.common.utils.JwtUtil;
import com.lyh.onlineShop.constant.JwtClaimsConstant;
import com.lyh.onlineShop.constant.UserRoleConstant;
import com.lyh.onlineShop.user.dto.UserLoginDto;
import com.lyh.onlineShop.user.service.UserService;
import com.lyh.onlineShop.common.entity.User;
import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import com.lyh.onlineShop.common.utils.Result;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyh
 */
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     *  注册
     * @param userLoginDto
     * @return
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody UserLoginDto userLoginDto) {
        userService.register(userLoginDto);
        return Result.success();
    }

    /**
     *  用户登录
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login")
    public Result userLogin(@Valid @RequestBody UserLoginDto userLoginDto) {
        User user = userService.login(userLoginDto);
        // 将生成的token返回给前端
        return Result.success(createToken(user));
    }

    /**
     *  管理员登录
     * @param userLoginDto
     * @return
     */
    @PostMapping("/adminLogin")
    public Result adminLogin(@Valid @RequestBody UserLoginDto userLoginDto) {
        User user = userService.login(userLoginDto);
        // 检查是否是管理员用户
        if(!user.getRole().equals(UserRoleConstant.ROLE_ADMIN)) {
            return Result.error(ExceptionEnum.USER_NEED_ADMIN);
        }
        // 将生成的token返回给前端
        return Result.success(createToken(user));
    }

    /**
     *  更新用户信息
     * @param userLoginDto
     * @return
     */
    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody UserLoginDto userLoginDto, @RequestHeader(JwtClaimsConstant.USER_ID) String userIdStr) {
        // 从请求头中获取用户id
        Integer userId = Integer.valueOf(userIdStr);
        User user = new User();
        BeanUtils.copyProperties(userLoginDto, user);
        user.setId(userId);
        userService.updateUser(user);
        return Result.success();
    }

//    /**
//     *  退出登录
//     * @param session
//     * @return
//     */
//    @PostMapping("/logout")
//    public Result logout(HttpSession session) {
//        session.removeAttribute(SessionConstant.SESSION_USER_KEY);
//        return Result.success();
//    }

    /*
        生成Jwt辅助方法
     */
    private String createToken(User user) {
        // 生成Jwt并返回
        Map<String, Object> dataMap = new HashMap<>();
        // 添加自定义信息到token中
        dataMap.put(JwtClaimsConstant.USER_ID, user.getId()); // 添加用户id
        dataMap.put(JwtClaimsConstant.USER_NAME, user.getUsername()); // 添加用户名
        dataMap.put(JwtClaimsConstant.USER_ROLE, user.getRole()); // 添加用户角色信息
        // 生成当前登录用户的Jwt并返回
        return JwtUtil.generateToken(jwtProperties.getSecretKey(), jwtProperties.getExpireTime(), dataMap);
    }
}
