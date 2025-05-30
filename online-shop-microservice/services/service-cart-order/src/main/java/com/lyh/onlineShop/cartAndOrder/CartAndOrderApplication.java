package com.lyh.onlineShop.cartAndOrder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lyh
 */
@EnableFeignClients //开启Feign远程调用功能
@SpringBootApplication(scanBasePackages = "com.lyh.onlineShop") // 扫描com.lyh.onlineShop包及其子包下的所有组件
public class CartAndOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartAndOrderApplication.class, args);
    }
}
