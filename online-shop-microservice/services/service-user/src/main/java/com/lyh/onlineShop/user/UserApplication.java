package com.lyh.onlineShop.user;

import com.lyh.onlineShop.common.properties.JwtProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author lyh
 */
//@EnableConfigurationProperties(JwtProperties.class) // 解决多模块项目Bean不生效的问题，显示导入JwtProperties
@MapperScan("com.lyh.onlineShop.user.mapper") // 扫描mapper接口
@SpringBootApplication(scanBasePackages = "com.lyh.onlineShop") // 扫描com.lyh.onlineShop包及子包下的所有组件（在pom文件中引入了common模块，所以可以扫描common包下的所有组件）
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
