package com.lyh.onlineShop.productAndCategory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lyh
 *  WebMvc配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${lyh.file.image.path}")
    private String imageUploadPath;

    /**
     *  配置静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射图片资源
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + imageUploadPath); // file表示从本地文件系统映射
    }
}
