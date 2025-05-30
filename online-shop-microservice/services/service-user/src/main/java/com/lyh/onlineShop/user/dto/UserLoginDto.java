package com.lyh.onlineShop.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author lyh
 *  用户登录数据传输对象
 */
@Data
public class UserLoginDto {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;


}
