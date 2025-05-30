package com.lyh.onlineShop.common.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author lyh
 */
@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private Integer role;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
