package com.lyh.onlineShop.productAndCategory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lyh
 */
@SpringBootApplication(scanBasePackages = "com.lyh.onlineShop") // 扫描com.lyh.onlineShop包及其子包下的所有组件
public class ProductAndCategoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductAndCategoryApplication.class, args);
    }
}
